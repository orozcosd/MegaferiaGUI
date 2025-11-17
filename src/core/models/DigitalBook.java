package core.models;

import java.util.ArrayList;

public class DigitalBook extends Book {

    private boolean hasHyperlink;
    private String hyperlink;

    public DigitalBook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.hasHyperlink = false;
        this.hyperlink = null;
    }

    public DigitalBook(String title, ArrayList<Author> authors, String isbn, String genre, String format, double value, Publisher publisher, String hyperlink) {
        super(title, authors, isbn, genre, format, value, publisher);
        this.hasHyperlink = true;
        this.hyperlink = hyperlink;
    }

    public boolean hasHyperlink() {
        return hasHyperlink;
    }

    public String getHyperlink() {
        return hyperlink;
    }

}
