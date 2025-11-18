package core.models.storage;

import core.models.Book;
import core.models.Author;
import java.util.ArrayList;

public interface IBookStorage {

    boolean addBook(Book book);

    Book getBook(String isbn);

    ArrayList<Book> getAllBooks();

    ArrayList<Book> getBooksByType(Class<?> type);

    ArrayList<Book> getBooksByAuthor(Author author);

    ArrayList<Book> getBooksByFormat(String format);

    boolean deleteBook(String isbn);

}
