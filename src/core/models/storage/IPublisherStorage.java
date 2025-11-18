package core.models.storage;

import core.models.Publisher;
import java.util.ArrayList;

public interface IPublisherStorage {

    boolean addPublisher(Publisher publisher);

    Publisher getPublisher(String nit);

    ArrayList<Publisher> getAllPublishers();

    boolean deletePublisher(String nit);

}
