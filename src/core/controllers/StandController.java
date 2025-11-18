package core.controllers;

import core.models.Stand;
import core.models.storage.StandStorage;
import core.utils.Response;
import core.utils.Status;
import core.utils.Validator;
import java.util.ArrayList;

public class StandController {

    public Response createStand(long id, double price) {
        if (!Validator.isValidId(id)) {
            return new Response("El ID del stand debe ser >= 0 y tener como máximo 15 dígitos", Status.BAD_REQUEST);
        }

        if (!Validator.isValidPrice(price)) {
            return new Response("El precio del stand debe ser mayor que 0", Status.BAD_REQUEST);
        }

        if (StandStorage.getInstance().getStand(id) != null) {
            return new Response("Ya existe un stand con ID " + id, Status.BAD_REQUEST);
        }

        Stand stand = new Stand(id, price);
        boolean added = StandStorage.getInstance().addStand(stand);

        if (added) {
            return new Response("Stand creado exitosamente", Status.CREATED, stand);
        } else {
            return new Response("Error al crear el stand", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllStands() {
        ArrayList<Stand> stands = StandStorage.getInstance().getAllStands();

        if (stands.isEmpty()) {
            return new Response("No se encontraron stands", Status.NO_CONTENT);
        }

        return new Response("Stands obtenidos exitosamente", Status.OK, stands);
    }

    public Response getStandById(long id) {
        Stand stand = StandStorage.getInstance().getStand(id);

        if (stand == null) {
            return new Response("Stand no encontrado", Status.NOT_FOUND);
        }

        return new Response("Stand obtenido exitosamente", Status.OK, stand);
    }

}
