package core.models.storage;

import core.models.Book;
import core.models.Author;
import java.util.ArrayList;

public class BookStorage {

    private static BookStorage instance;

    private ArrayList<Book> books;

    private BookStorage() {
        this.books = new ArrayList<>();
    }

    public static BookStorage getInstance() {
        if (instance == null) {
            instance = new BookStorage();
        }
        return instance;
    }

    public boolean addBook(Book book) {
        for (Book b : this.books) {
            if (b.getIsbn().equals(book.getIsbn())) {
                return false;
            }
        }
        this.books.add(book);
        return true;
    }

    public Book getBook(String isbn) {
        for (Book book : this.books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(this.books);
    }

    public ArrayList<Book> getBooksByType(Class<?> type) {
        // Esto recibe una clase sin instanciar para poder saber el tipo del libro almacenado
        ArrayList<Book> filteredBooks = new ArrayList<>();
        for (Book book : this.books) {
            if (type.isInstance(book)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public ArrayList<Book> getBooksByAuthor(Author author) {
        ArrayList<Book> filteredBooks = new ArrayList<>();
        for (Book book : this.books) {
            if (book.getAuthors().contains(author)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public ArrayList<Book> getBooksByFormat(String format) {
        ArrayList<Book> filteredBooks = new ArrayList<>();
        for (Book book : this.books) {
            if (book.getFormat().equalsIgnoreCase(format)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public boolean deleteBook(String isbn) {
        for (Book book : this.books) {
            if (book.getIsbn().equals(isbn)) {
                this.books.remove(book);
                return true;
            }
        }
        return false;
    }

}
