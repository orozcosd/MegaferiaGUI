package core.controllers;

import core.models.Author;
import core.models.Audiobook;
import core.models.Book;
import core.models.DigitalBook;
import core.models.PrintedBook;
import core.models.storage.BookStorage;
import core.models.storage.PersonStorage;
import core.utils.Response;
import core.utils.Status;
import java.util.ArrayList;

public class QueryController {

    private BookStorage bookStorage;
    private PersonStorage personStorage;

    public QueryController() {
        this.bookStorage = BookStorage.getInstance();
        this.personStorage = PersonStorage.getInstance();
    }

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

        ArrayList<Book> books = bookStorage.getBooksByType(type);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el tipo: " + bookType, Status.NO_CONTENT);
        }

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getBooksByAuthor(long authorId) {
        Author author = (Author) personStorage.getPerson(authorId);

        if (author == null) {
            return new Response("Autor con ID " + authorId + " no encontrado", Status.NOT_FOUND);
        }

        if (!(author instanceof Author)) {
            return new Response("La persona con ID " + authorId + " no es un autor", Status.BAD_REQUEST);
        }

        ArrayList<Book> books = bookStorage.getBooksByAuthor(author);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el autor: " + author.getFullname(), Status.NO_CONTENT);
        }

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getBooksByFormat(String format) {
        if (format == null || format.trim().isEmpty()) {
            return new Response("El formato no puede estar vacío", Status.BAD_REQUEST);
        }

        ArrayList<Book> books = bookStorage.getBooksByFormat(format);

        if (books.isEmpty()) {
            return new Response("No se encontraron libros para el formato: " + format, Status.NO_CONTENT);
        }

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getAllBooks() {
        ArrayList<Book> books = bookStorage.getAllBooks();

        if (books.isEmpty()) {
            return new Response("No se encontraron libros", Status.NO_CONTENT);
        }

        return new Response("Libros obtenidos exitosamente", Status.OK, books);
    }

    public Response getAuthorsWithMostBooksInDifferentPublishers() {
        ArrayList<Author> authors = personStorage.getAuthors();

        if (authors.isEmpty()) {
            return new Response("No se encontraron autores", Status.NO_CONTENT);
        }

        return new Response("Autores obtenidos exitosamente", Status.OK, authors);
    }

}
