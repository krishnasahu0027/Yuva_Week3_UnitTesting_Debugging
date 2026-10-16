import java.time.Year;

/**
 * Represents one book in the library inventory.
 * Validation is performed before a property is stored.
 */
public class Book {
    private final int id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;

    public Book(int id, String title, String author, String isbn, int publicationYear) {
        if (id < 1) {
            throw new IllegalArgumentException("Book ID must be positive.");
        }
        this.id = id;
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        setPublicationYear(publicationYear);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        this.author = author.trim();
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty.");
        }
        this.isbn = isbn.trim();
    }

    public void setPublicationYear(int year) {
        if (year < 1 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Enter a valid publication year.");
        }
        this.publicationYear = year;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Title: %s | Author: %s | ISBN: %s | Year: %d",
                id, title, author, isbn, publicationYear);
    }
}
