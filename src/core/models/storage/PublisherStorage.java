package core.models.storage;

import core.models.Publisher;
import java.util.ArrayList;

public class PublisherStorage {

    private static PublisherStorage instance;

    private ArrayList<Publisher> publishers;

    private PublisherStorage() {
        this.publishers = new ArrayList<>();
    }

    public static PublisherStorage getInstance() {
        if (instance == null) {
            instance = new PublisherStorage();
        }
        return instance;
    }

    public boolean addPublisher(Publisher publisher) {
        for (Publisher p : this.publishers) {
            if (p.getNit().equals(publisher.getNit())) {
                return false;
            }
        }
        this.publishers.add(publisher);
        return true;
    }

    public Publisher getPublisher(String nit) {
        for (Publisher publisher : this.publishers) {
            if (publisher.getNit().equals(nit)) {
                return publisher;
            }
        }
        return null;
    }

    public ArrayList<Publisher> getAllPublishers() {
        return new ArrayList<>(this.publishers);
    }

    public boolean deletePublisher(String nit) {
        for (Publisher publisher : this.publishers) {
            if (publisher.getNit().equals(nit)) {
                this.publishers.remove(publisher);
                return true;
            }
        }
        return false;
    }

}
