package core.controllers;

import core.models.Author;
import core.models.Audiobook;
import core.models.Book;
import core.models.DigitalBook;
import core.models.PrintedBook;
import core.models.Publisher;
import core.models.storage.BookStorage;
import core.models.storage.PersonStorage;
import core.models.storage.PublisherStorage;
import core.utils.Response;
import core.utils.Status;
import java.util.ArrayList;
import java.util.Comparator;

public class QueryController {

    public Response getBooksByType(String bookType) {
        Class<?> type;

        switch (bookType.toLowerCase()) {
            case "printed":
                type = PrintedBook.class;
                break;
            case "digital":
                type = DigitalBook.class;
                break;
            case "audiobook":
                type = Audiobook.class;
                break;
            default:
                return new Response("Tipo de libro inválido. Los tipos válidos son: printed, digital, audiobook", Status.BAD_REQUEST);
        }

        ArrayList<Book> books = BookStorage.getInstance().getBooksByType(type);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el tipo: " + bookType, Status.NO_CONTENT);
        }

        books.sort(Comparator.comparing(Book::getIsbn));

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getBooksByAuthor(String authorIdText) {
        long authorId;

        try {
            authorId = Long.parseLong(authorIdText);
        } catch (NumberFormatException e) {
            return new Response("El ID del autor debe ser un número válido", Status.BAD_REQUEST);
        }

        Author author = (Author) PersonStorage.getInstance().getPerson(authorId);

        if (author == null) {
            return new Response("Autor con ID " + authorId + " no encontrado", Status.NOT_FOUND);
        }

        if (!(author instanceof Author)) {
            return new Response("La persona con ID " + authorId + " no es un autor", Status.BAD_REQUEST);
        }

        ArrayList<Book> books = BookStorage.getInstance().getBooksByAuthor(author);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el autor: " + author.getFullname(), Status.NO_CONTENT);
        }

        books.sort(Comparator.comparing(Book::getIsbn));

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getBooksByFormat(String format) {
        if (format == null || format.trim().isEmpty()) {
            return new Response("El formato no puede estar vacío", Status.BAD_REQUEST);
        }

        ArrayList<Book> books = BookStorage.getInstance().getBooksByFormat(format);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el formato: " + format, Status.NO_CONTENT);
        }

        books.sort(Comparator.comparing(Book::getIsbn));

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getAllBooks() {
        ArrayList<Book> books = BookStorage.getInstance().getAllBooks();

        if (books.isEmpty()) {
            return new Response("No se encontraron libros", Status.NO_CONTENT);
        }

        books.sort(Comparator.comparing(Book::getIsbn));

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getAuthorsDifferentPublishers() {
        ArrayList<Publisher> todasEditoriales = PublisherStorage.getInstance().getAllPublishers();

        if (todasEditoriales.isEmpty()) {
            return new Response("No se encontraron editoriales", Status.NO_CONTENT);
        }

        ArrayList<Author> autoresConMasLibros = new ArrayList<>();

        for (Publisher editorial : todasEditoriales) {
            ArrayList<Book> librosEditorial = editorial.getBooks();

            if (librosEditorial.isEmpty()) {
                continue;
            }

            ArrayList<Author> autoresEnEditorial = new ArrayList<>();
            ArrayList<Integer> conteoLibros = new ArrayList<>();

            for (Book libro : librosEditorial) {
                for (Author autor : libro.getAuthors()) {
                    int indice = autoresEnEditorial.indexOf(autor);
                    if (indice == -1) {
                        autoresEnEditorial.add(autor);
                        conteoLibros.add(1);
                    } else {
                        conteoLibros.set(indice, conteoLibros.get(indice) + 1);
                    }
                }
            }

            int maxLibros = -1;
            for (int conteo : conteoLibros) {
                if (conteo > maxLibros) {
                    maxLibros = conteo;
                }
            }

            for (int i = 0; i < autoresEnEditorial.size(); i++) {
                if (conteoLibros.get(i) == maxLibros) {
                    Author autor = autoresEnEditorial.get(i);
                    if (!autoresConMasLibros.contains(autor)) {
                        autoresConMasLibros.add(autor);
                    }
                }
            }
        }

        if (autoresConMasLibros.isEmpty()) {
            return new Response("No se encontraron autores con libros", Status.NO_CONTENT);
        }

        autoresConMasLibros.sort(Comparator.comparingLong(Author::getId));

        return new Response("Autores obtenidos exitosamente", Status.OK, autoresConMasLibros);
    }

}
