package core.controllers;

import core.models.Publisher;
import core.models.Stand;
import core.models.storage.PublisherStorage;
import core.models.storage.StandStorage;
import core.utils.Response;
import core.utils.Status;
import java.util.HashSet;

public class StandPurchaseController {

    public Response purchaseStands(String[] standIdTexts, String[] publisherNits) {
        if (standIdTexts == null || standIdTexts.length == 0) {
            return new Response("Debe seleccionar al menos un stand", Status.BAD_REQUEST);
        }

        if (publisherNits == null || publisherNits.length == 0) {
            return new Response("Debe seleccionar al menos una editorial", Status.BAD_REQUEST);
        }

        long[] standIds = new long[standIdTexts.length];
        try {
            for (int i = 0; i < standIdTexts.length; i++) {
                standIds[i] = Long.parseLong(standIdTexts[i]);
            }
        } catch (NumberFormatException e) {
            return new Response("Los IDs de los stands deben ser números válidos", Status.BAD_REQUEST);
        }

        HashSet<Long> uniqueStandIds = new HashSet<>();
        for (long standId : standIds) {
            if (!uniqueStandIds.add(standId)) {
                return new Response("ID de stand duplicado encontrado: " + standId, Status.BAD_REQUEST);
            }
        }

        HashSet<String> uniquePublisherNits = new HashSet<>();
        for (String nit : publisherNits) {
            if (!uniquePublisherNits.add(nit)) {
                return new Response("NIT de editorial duplicado encontrado: " + nit, Status.BAD_REQUEST);
            }
        }

        for (long standId : standIds) {
            Stand stand = StandStorage.getInstance().getStand(standId);
            if (stand == null) {
                return new Response("Stand con ID " + standId + " no encontrado", Status.NOT_FOUND);
            }
        }

        for (String nit : publisherNits) {
            Publisher publisher = PublisherStorage.getInstance().getPublisher(nit);
            if (publisher == null) {
                return new Response("Editorial con NIT " + nit + " no encontrada", Status.NOT_FOUND);
            }
        }

        for (long standId : standIds) {
            Stand stand = StandStorage.getInstance().getStand(standId);
            for (String nit : publisherNits) {
                Publisher publisher = PublisherStorage.getInstance().getPublisher(nit);
                stand.addPublisher(publisher);
                publisher.addStand(stand);
            }
        }

        String message = "Se vincularon exitosamente " + standIds.length + " stand(s) con " + publisherNits.length + " editorial(es)";
        return new Response(message, Status.OK);
    }

}
