package core.controllers;

import core.models.Manager;
import core.models.Publisher;
import core.models.storage.PersonStorage;
import core.models.storage.PublisherStorage;
import core.utils.Response;
import core.utils.Status;
import core.utils.Validator;
import java.util.ArrayList;
import java.util.Comparator;

public class PublisherController {

    public Response createPublisher(String nit, String name, String address, String managerIdText) {
        long managerId;

        try {
            managerId = Long.parseLong(managerIdText);
        } catch (NumberFormatException e) {
            return new Response("El ID del gerente debe ser un número válido", Status.BAD_REQUEST);
        }

        if (!Validator.isValidNit(nit)) {
            return new Response("El NIT de la editorial debe seguir el formato XXX.XXX.XXX-X", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(name)) {
            return new Response("El nombre de la editorial no puede estar vacío", Status.BAD_REQUEST);
        }

        if (!Validator.isNotEmpty(address)) {
            return new Response("La dirección de la editorial no puede estar vacía", Status.BAD_REQUEST);
        }

        if (PublisherStorage.getInstance().getPublisher(nit) != null) {
            return new Response("Ya existe una editorial con NIT " + nit, Status.BAD_REQUEST);
        }

        Manager manager = (Manager) PersonStorage.getInstance().getPerson(managerId);
        if (manager == null) {
            return new Response("Gerente con ID " + managerId + " no encontrado", Status.NOT_FOUND);
        }

        if (!(manager instanceof Manager)) {
            return new Response("La persona con ID " + managerId + " no es un gerente", Status.BAD_REQUEST);
        }

        Publisher publisher = new Publisher(nit, name, address, manager);
        boolean added = PublisherStorage.getInstance().addPublisher(publisher);

        if (added) {
            return new Response("Editorial creada exitosamente", Status.CREATED, publisher);
        } else {
            return new Response("Error al crear la editorial", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllPublishers() {
        ArrayList<Publisher> publishers = PublisherStorage.getInstance().getAllPublishers();

        if (publishers.isEmpty()) {
            return new Response("No se encontraron editoriales", Status.NO_CONTENT);
        }

        publishers.sort(Comparator.comparing(Publisher::getNit));

        return new Response("Editoriales obtenidas exitosamente", Status.OK, publishers);
    }

    public Response getPublisherByNit(String nit) {
        Publisher publisher = PublisherStorage.getInstance().getPublisher(nit);

        if (publisher == null) {
            return new Response("Editorial no encontrada", Status.NOT_FOUND);
        }

        return new Response("Editorial obtenida exitosamente", Status.OK, publisher);
    }

}
