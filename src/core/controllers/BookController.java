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

    public Response createPrintedBook(String title, String[] authorIdTexts, String isbn, String genre,
                                     String format, String valueText, String publisherNit,
                                     String pagesText, String copiesText) {
        double value;
        int pages;
        int copies;

        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException e) {
            return new Response("El precio debe ser un número válido", Status.BAD_REQUEST);
        }

        try {
            pages = Integer.parseInt(pagesText);
        } catch (NumberFormatException e) {
            return new Response("El número de páginas debe ser un número válido", Status.BAD_REQUEST);
        }

        try {
            copies = Integer.parseInt(copiesText);
        } catch (NumberFormatException e) {
            return new Response("El número de copias debe ser un número válido", Status.BAD_REQUEST);
        }

        long[] authorIds;
        try {
            authorIds = parseAuthorIds(authorIdTexts);
        } catch (NumberFormatException e) {
            return new Response("Los IDs de los autores deben ser números válidos", Status.BAD_REQUEST);
        }

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
        Publisher publisher = PublisherStorage.getInstance().getPublisher(publisherNit);

        PrintedBook book = new PrintedBook(title, authors, isbn, genre, format, value, publisher, pages, copies);
        boolean added = BookStorage.getInstance().addBook(book);

        if (added) {
            return new Response("Libro impreso creado exitosamente", Status.CREATED, book);
        } else {
            return new Response("Error al crear el libro impreso", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createDigitalBook(String title, String[] authorIdTexts, String isbn, String genre,
                                     String format, String valueText, String publisherNit,
                                     String hyperlink) {
        double value;

        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException e) {
            return new Response("El precio debe ser un número válido", Status.BAD_REQUEST);
        }

        long[] authorIds;
        try {
            authorIds = parseAuthorIds(authorIdTexts);
        } catch (NumberFormatException e) {
            return new Response("Los IDs de los autores deben ser números válidos", Status.BAD_REQUEST);
        }

        Response validation = validateCommonBookFields(title, authorIds, isbn, genre, format, value, publisherNit);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        ArrayList<Author> authors = getAuthorsFromIds(authorIds);
        Publisher publisher = PublisherStorage.getInstance().getPublisher(publisherNit);

        DigitalBook book;
        if (hyperlink == null || hyperlink.trim().isEmpty()) {
            book = new DigitalBook(title, authors, isbn, genre, format, value, publisher);
        } else {
            book = new DigitalBook(title, authors, isbn, genre, format, value, publisher, hyperlink);
        }

        boolean added = BookStorage.getInstance().addBook(book);

        if (added) {
            return new Response("Libro digital creado exitosamente", Status.CREATED, book);
        } else {
            return new Response("Error al crear el libro digital", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createAudiobook(String title, String[] authorIdTexts, String isbn, String genre,
                                   String format, String valueText, String publisherNit,
                                   String durationText, String narratorIdText) {
        double value;
        int duration;
        long narratorId;

        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException e) {
            return new Response("El precio debe ser un número válido", Status.BAD_REQUEST);
        }

        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            return new Response("La duración debe ser un número válido", Status.BAD_REQUEST);
        }

        try {
            narratorId = Long.parseLong(narratorIdText);
        } catch (NumberFormatException e) {
            return new Response("El ID del narrador debe ser un número válido", Status.BAD_REQUEST);
        }

        long[] authorIds;
        try {
            authorIds = parseAuthorIds(authorIdTexts);
        } catch (NumberFormatException e) {
            return new Response("Los IDs de los autores deben ser números válidos", Status.BAD_REQUEST);
        }

        Response validation = validateCommonBookFields(title, authorIds, isbn, genre, format, value, publisherNit);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        if (!Validator.isPositive(duration)) {
            return new Response("La duración debe ser mayor que 0", Status.BAD_REQUEST);
        }

        Narrator narrator = (Narrator) PersonStorage.getInstance().getPerson(narratorId);
        if (narrator == null) {
            return new Response("Narrador con ID " + narratorId + " no encontrado", Status.NOT_FOUND);
        }

        if (!(narrator instanceof Narrator)) {
            return new Response("La persona con ID " + narratorId + " no es un narrador", Status.BAD_REQUEST);
        }

        ArrayList<Author> authors = getAuthorsFromIds(authorIds);
        Publisher publisher = PublisherStorage.getInstance().getPublisher(publisherNit);

        Audiobook book = new Audiobook(title, authors, isbn, genre, format, value, publisher, duration, narrator);
        boolean added = BookStorage.getInstance().addBook(book);

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

        if (BookStorage.getInstance().getBook(isbn) != null) {
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
            Author author = (Author) PersonStorage.getInstance().getPerson(authorId);
            if (author == null) {
                return new Response("Autor con ID " + authorId + " no encontrado", Status.NOT_FOUND);
            }
            if (!(author instanceof Author)) {
                return new Response("La persona con ID " + authorId + " no es un autor", Status.BAD_REQUEST);
            }
        }

        Publisher publisher = PublisherStorage.getInstance().getPublisher(publisherNit);
        if (publisher == null) {
            return new Response("Editorial con NIT " + publisherNit + " no encontrada", Status.NOT_FOUND);
        }

        return new Response("Validación exitosa", Status.OK);
    }

    private ArrayList<Author> getAuthorsFromIds(long[] authorIds) {
        ArrayList<Author> authors = new ArrayList<>();
        for (long authorId : authorIds) {
            authors.add((Author) PersonStorage.getInstance().getPerson(authorId));
        }
        return authors;
    }

    private long[] parseAuthorIds(String[] authorIdTexts) throws NumberFormatException {
        if (authorIdTexts == null) {
            return new long[0];
        }
        long[] authorIds = new long[authorIdTexts.length];
        for (int i = 0; i < authorIdTexts.length; i++) {
            authorIds[i] = Long.parseLong(authorIdTexts[i]);
        }
        return authorIds;
    }

}
