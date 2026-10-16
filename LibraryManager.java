import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contains the library inventory operations.
 * Data is stored in memory using an ArrayList.
 */
public class LibraryManager {
    private final List<Book> books = new ArrayList<Book>();
    private int nextId = 1;

    /** Creates a book after checking ISBN uniqueness. */
    public Book addBook(String title, String author, String isbn, int year) {
        if (findByIsbn(isbn).isPresent()) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }
        Book book = new Book(nextId, title, author, isbn, year);
        books.add(book);
        nextId++;
        return book;
    }

    /** Returns a defensive copy of the current inventory list. */
    public List<Book> getAllBooks() {
        return new ArrayList<Book>(books);
    }

    public Optional<Book> findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    private Optional<Book> findByIsbn(String isbn) {
        if (isbn == null) {
            return Optional.empty();
        }
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn.trim())) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    /** Updates an existing record only after validating all replacement values. */
    public boolean updateBook(int id, String title, String author, String isbn, int year) {
        Optional<Book> match = findById(id);
        if (!match.isPresent()) {
            return false;
        }

        Optional<Book> duplicate = findByIsbn(isbn);
        if (duplicate.isPresent() && duplicate.get().getId() != id) {
            throw new IllegalArgumentException("Another book already uses this ISBN.");
        }

        // Build a temporary object first so invalid values cannot partially update a record.
        Book validated = new Book(id, title, author, isbn, year);
        Book current = match.get();
        current.setTitle(validated.getTitle());
        current.setAuthor(validated.getAuthor());
        current.setIsbn(validated.getIsbn());
        current.setPublicationYear(validated.getPublicationYear());
        return true;
    }

    public boolean deleteBook(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
                return true;
            }
        }
        return false;
    }
}
