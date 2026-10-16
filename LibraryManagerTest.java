import static org.junit.jupiter.api.Assertions.*;

import java.time.Year;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for book validation and library CRUD operations. */
public class LibraryManagerTest {
    private LibraryManager library;

    @BeforeEach
    void setUp() {
        library = new LibraryManager();
    }

    @Test
    void addBookAssignsIdAndStoresBook() {
        Book book = library.addBook("Atomic Habits", "James Clear", "9781", 2018);
        assertEquals(1, book.getId());
        assertEquals(1, library.getAllBooks().size());
        assertEquals("Atomic Habits", library.findById(1).get().getTitle());
    }

    @Test
    void idsIncreaseForEachAddedBook() {
        Book first = library.addBook("Book A", "Author A", "ISBN-A", 2020);
        Book second = library.addBook("Book B", "Author B", "ISBN-B", 2021);
        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
    }

    @Test
    void listReturnsAllBooksAndDefensiveListCopy() {
        library.addBook("Book A", "Author A", "ISBN-A", 2020);
        library.addBook("Book B", "Author B", "ISBN-B", 2021);
        List<Book> result = library.getAllBooks();
        assertEquals(2, result.size());
        result.clear();
        assertEquals(2, library.getAllBooks().size());
    }

    @Test
    void searchExistingAndMissingIds() {
        library.addBook("Book A", "Author A", "ISBN-A", 2020);
        assertTrue(library.findById(1).isPresent());
        assertFalse(library.findById(999).isPresent());
    }

    @Test
    void updateChangesFieldsAndKeepsId() {
        library.addBook("Old Title", "Old Author", "ISBN-A", 2020);
        assertTrue(library.updateBook(1, "New Title", "New Author", "ISBN-B", 2022));
        Book updated = library.findById(1).get();
        assertEquals(1, updated.getId());
        assertEquals("New Title", updated.getTitle());
        assertEquals("New Author", updated.getAuthor());
        assertEquals("ISBN-B", updated.getIsbn());
        assertEquals(2022, updated.getPublicationYear());
    }

    @Test
    void updateMissingIdReturnsFalse() {
        assertFalse(library.updateBook(99, "Title", "Author", "ISBN", 2020));
    }

    @Test
    void deleteExistingAndMissingIds() {
        library.addBook("Book A", "Author A", "ISBN-A", 2020);
        assertTrue(library.deleteBook(1));
        assertTrue(library.getAllBooks().isEmpty());
        assertFalse(library.deleteBook(1));
    }

    @Test
    void rejectDuplicateIsbnIgnoringCaseAndWhitespace() {
        library.addBook("Book A", "Author A", "isbn-1", 2020);
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("Book B", "Author B", " ISBN-1 ", 2021));
    }

    @Test
    void rejectUpdatingToAnotherBooksIsbnWithoutChangingOriginal() {
        library.addBook("Book A", "Author A", "ISBN-A", 2020);
        library.addBook("Book B", "Author B", "ISBN-B", 2021);
        assertThrows(IllegalArgumentException.class,
                () -> library.updateBook(1, "Changed", "Changed Author", "ISBN-B", 2022));
        assertEquals("Book A", library.findById(1).get().getTitle());
        assertEquals("ISBN-A", library.findById(1).get().getIsbn());
    }

    @Test
    void rejectBlankBookFields() {
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("  ", "Author", "ISBN", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("Title", null, "ISBN", 2020));
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("Title", "Author", " ", 2020));
    }

    @Test
    void rejectInvalidPublicationYears() {
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("Title", "Author", "ISBN-1", 0));
        assertThrows(IllegalArgumentException.class,
                () -> library.addBook("Title", "Author", "ISBN-2", Year.now().getValue() + 1));
    }

    @Test
    void invalidUpdateDoesNotPartiallyChangeBook() {
        library.addBook("Original", "Author", "ISBN-A", 2020);
        assertThrows(IllegalArgumentException.class,
                () -> library.updateBook(1, "Changed", "Author", "ISBN-B", 0));
        Book unchanged = library.findById(1).get();
        assertEquals("Original", unchanged.getTitle());
        assertEquals("ISBN-A", unchanged.getIsbn());
        assertEquals(2020, unchanged.getPublicationYear());
    }

    @Test
    void trimsTextInput() {
        Book book = library.addBook("  Clean Title  ", "  Author  ", "  ISBN-A  ", 2020);
        assertEquals("Clean Title", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals("ISBN-A", book.getIsbn());
    }

    @Test
    void bookRejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book(0, "Title", "Author", "ISBN", 2020));
    }
}
