package core.models;

public class Manager extends Person {

    private Publisher publisher;

    public Manager(long id, String firstname, String lastname) {
        super(id, firstname, lastname);
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

}
