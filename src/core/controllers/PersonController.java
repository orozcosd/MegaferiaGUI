package core.controllers;

import core.models.Author;
import core.models.Manager;
import core.models.Narrator;
import core.models.Person;
import core.models.storage.PersonStorage;
import core.utils.Response;
import core.utils.Status;
import core.utils.Validator;
import java.util.ArrayList;
import java.util.Comparator;

public class PersonController {

    public Response createAuthor(long id, String firstname, String lastname) {
        if (!Validator.isValidId(id)) {
            return new Response("El ID de la persona debe ser >= 0 y tener como máximo 15 dígitos", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(firstname)) {
            return new Response("El nombre no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(lastname)) {
            return new Response("El apellido no puede estar vacío", Status.BAD_REQUEST);
        }

        if (PersonStorage.getInstance().getPerson(id) != null) {
            return new Response("Ya existe una persona con ID " + id, Status.BAD_REQUEST);
        }

        Author author = new Author(id, firstname, lastname);
        boolean added = PersonStorage.getInstance().addPerson(author);

        if (added) {
            return new Response("Autor creado exitosamente", Status.CREATED, author);
        } else {
            return new Response("Error al crear el autor", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createManager(long id, String firstname, String lastname) {
        if (!Validator.isValidId(id)) {
            return new Response("El ID de la persona debe ser >= 0 y tener como máximo 15 dígitos", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(firstname)) {
            return new Response("El nombre no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(lastname)) {
            return new Response("El apellido no puede estar vacío", Status.BAD_REQUEST);
        }

        if (PersonStorage.getInstance().getPerson(id) != null) {
            return new Response("Ya existe una persona con ID " + id, Status.BAD_REQUEST);
        }

        Manager manager = new Manager(id, firstname, lastname);
        boolean added = PersonStorage.getInstance().addPerson(manager);

        if (added) {
            return new Response("Gerente creado exitosamente", Status.CREATED, manager);
        } else {
            return new Response("Error al crear el gerente", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response createNarrator(long id, String firstname, String lastname) {
        if (!Validator.isValidId(id)) {
            return new Response("El ID de la persona debe ser >= 0 y tener como máximo 15 dígitos", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(firstname)) {
            return new Response("El nombre no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(lastname)) {
            return new Response("El apellido no puede estar vacío", Status.BAD_REQUEST);
        }

        if (PersonStorage.getInstance().getPerson(id) != null) {
            return new Response("Ya existe una persona con ID " + id, Status.BAD_REQUEST);
        }

        Narrator narrator = new Narrator(id, firstname, lastname);
        boolean added = PersonStorage.getInstance().addPerson(narrator);

        if (added) {
            return new Response("Narrador creado exitosamente", Status.CREATED, narrator);
        } else {
            return new Response("Error al crear el narrador", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllPersons() {
        ArrayList<Person> persons = PersonStorage.getInstance().getAllPersons();

        if (persons.isEmpty()) {
            return new Response("No se encontraron personas", Status.NO_CONTENT);
        }

        persons.sort(Comparator.comparingLong(Person::getId));

        return new Response("Personas obtenidas exitosamente", Status.OK, persons);
    }

    public Response getPersonById(long id) {
        Person person = PersonStorage.getInstance().getPerson(id);

        if (person == null) {
            return new Response("Persona no encontrada", Status.NOT_FOUND);
        }

        return new Response("Persona obtenida exitosamente", Status.OK, person);
    }

    public Response getAllAuthors() {
        ArrayList<Author> authors = PersonStorage.getInstance().getAuthors();

        if (authors.isEmpty()) {
            return new Response("No se encontraron autores", Status.NO_CONTENT);
        }

        authors.sort(Comparator.comparingLong(Author::getId));

        return new Response("Autores obtenidos exitosamente", Status.OK, authors);
    }

    public Response getAllManagers() {
        ArrayList<Manager> managers = PersonStorage.getInstance().getManagers();

        if (managers.isEmpty()) {
            return new Response("No se encontraron gerentes", Status.NO_CONTENT);
        }

        managers.sort(Comparator.comparingLong(Manager::getId));

        return new Response("Gerentes obtenidos exitosamente", Status.OK, managers);
    }

    public Response getAllNarrators() {
        ArrayList<Narrator> narrators = PersonStorage.getInstance().getNarrators();

        if (narrators.isEmpty()) {
            return new Response("No se encontraron narradores", Status.NO_CONTENT);
        }

        narrators.sort(Comparator.comparingLong(Narrator::getId));

        return new Response("Narradores obtenidos exitosamente", Status.OK, narrators);
    }

}
