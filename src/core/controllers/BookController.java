package core.controllers;

import core.models.Author;
import core.models.Audiobook;
import core.models.DigitalBook;
import core.models.Narrator;
import core.models.PrintedBook;
import core.models.Publisher;
import core.models.storage.BookStorage;
import core.models.storage.PersonStorage;
import core.models.storage.PublisherStorage;
import core.utils.Response;
import core.utils.Status;
import core.utils.Validator;
import java.util.ArrayList;
import java.util.HashSet;

public class BookController {

    private BookStorage bookStorage;
    private PersonStorage personStorage;
    private PublisherStorage publisherStorage;

    public BookController() {
        this.bookStorage = BookStorage.getInstance();
        this.personStorage = PersonStorage.getInstance();
        this.publisherStorage = PublisherStorage.getInstance();
    }

    public Response createPrintedBook(String title, long[] authorIds, String isbn, String genre,
                                     String format, double value, String publisherNit,
                                     int pages, int copies) {
        Response validation = validateCommonBookFields(title, authorIds, isbn, genre, format, value, publisherNit);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        if (!Validator.isPositive(pages)) {
            return new Response("El número de páginas debe ser mayor que 0", Status.BAD_REQUEST);
        }

        if (!Validator.isPositive(copies)) {
            return new Response("El número de copias debe ser mayor que 0", Status.BAD_REQUEST);
        }

        ArrayList<Author> authors = getAuthorsFromIds(authorIds);
        Publisher publisher = publisherStorage.getPublisher(publisherNit);

        PrintedBook book = new PrintedBook(title, authors, isbn, genre, format, value, publisher, pages, copies);
        boolean added = bookStorage.addBook(book);

        if (added) {
            return new Response("Libro impreso creado exitosamente", Status.CREATED, book);
        } else {
            return new Response("Error al crear el libro impreso", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createDigitalBook(String title, long[] authorIds, String isbn, String genre,
                                     String format, double value, String publisherNit,
                                     String hyperlink) {
        Response validation = validateCommonBookFields(title, authorIds, isbn, genre, format, value, publisherNit);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        ArrayList<Author> authors = getAuthorsFromIds(authorIds);
        Publisher publisher = publisherStorage.getPublisher(publisherNit);

        DigitalBook book;
        if (hyperlink == null || hyperlink.trim().isEmpty()) {
            book = new DigitalBook(title, authors, isbn, genre, format, value, publisher);
        } else {
            book = new DigitalBook(title, authors, isbn, genre, format, value, publisher, hyperlink);
        }

        boolean added = bookStorage.addBook(book);

        if (added) {
            return new Response("Libro digital creado exitosamente", Status.CREATED, book);
        } else {
            return new Response("Error al crear el libro digital", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createAudiobook(String title, long[] authorIds, String isbn, String genre,
                                   String format, double value, String publisherNit,
                                   int duration, long narratorId) {
        Response validation = validateCommonBookFields(title, authorIds, isbn, genre, format, value, publisherNit);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        if (!Validator.isPositive(duration)) {
            return new Response("La duración debe ser mayor que 0", Status.BAD_REQUEST);
        }

        Narrator narrator = (Narrator) personStorage.getPerson(narratorId);
        if (narrator == null) {
            return new Response("Narrador con ID " + narratorId + " no encontrado", Status.NOT_FOUND);
        }

        if (!(narrator instanceof Narrator)) {
            return new Response("La persona con ID " + narratorId + " no es un narrador", Status.BAD_REQUEST);
        }

        ArrayList<Author> authors = getAuthorsFromIds(authorIds);
        Publisher publisher = publisherStorage.getPublisher(publisherNit);

        Audiobook book = new Audiobook(title, authors, isbn, genre, format, value, publisher, duration, narrator);
        boolean added = bookStorage.addBook(book);

        if (added) {
            return new Response("Audiolibro creado exitosamente", Status.CREATED, book);
        } else {
            return new Response("Error al crear el audiolibro", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Response validateCommonBookFields(String title, long[] authorIds, String isbn,
                                             String genre, String format, double value,
                                             String publisherNit) {
        if (!Validator.isValidIsbn(isbn)) {
            return new Response("El ISBN debe seguir el formato XXX-X-XX-XXXXXX-X", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(title)) {
            return new Response("El título no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(genre)) {
            return new Response("El género no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(format)) {
            return new Response("El formato no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isValidPrice(value)) {
            return new Response("El precio del libro debe ser mayor que 0", Status.BAD_REQUEST);
        }

        if (bookStorage.getBook(isbn) != null) {
            return new Response("Ya existe un libro con ISBN " + isbn, Status.BAD_REQUEST);
        }

        if (authorIds == null || authorIds.length == 0) {
            return new Response("El libro debe tener al menos un autor", Status.BAD_REQUEST);
        }

        HashSet<Long> uniqueAuthors = new HashSet<>();
        for (long authorId : authorIds) {
            if (!uniqueAuthors.add(authorId)) {
                return new Response("ID de autor duplicado encontrado: " + authorId, Status.BAD_REQUEST);
            }
        }

        for (long authorId : authorIds) {
            Author author = (Author) personStorage.getPerson(authorId);
            if (author == null) {
                return new Response("Autor con ID " + authorId + " no encontrado", Status.NOT_FOUND);
            }
            if (!(author instanceof Author)) {
                return new Response("La persona con ID " + authorId + " no es un autor", Status.BAD_REQUEST);
            }
        }

        Publisher publisher = publisherStorage.getPublisher(publisherNit);
        if (publisher == null) {
            return new Response("Editorial con NIT " + publisherNit + " no encontrada", Status.NOT_FOUND);
        }

        return new Response("Validación exitosa", Status.OK);
    }

    private ArrayList<Author> getAuthorsFromIds(long[] authorIds) {
        ArrayList<Author> authors = new ArrayList<>();
        for (long authorId : authorIds) {
            authors.add((Author) personStorage.getPerson(authorId));
        }
        return authors;
    }

}
