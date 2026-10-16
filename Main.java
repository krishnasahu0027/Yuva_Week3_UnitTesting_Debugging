import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Command-line interface for the Library Book Inventory application. */
public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final LibraryManager LIBRARY = new LibraryManager();

    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("     LIBRARY BOOK INVENTORY");
        System.out.println("==================================");
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1: addBook(); break;
                    case 2: listBooks(); break;
                    case 3: updateBook(); break;
                    case 4: deleteBook(); break;
                    case 5: searchBook(); break;
                    case 0:
                        running = false;
                        System.out.println("Thank you. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Enter a number from 0 to 5.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Input error: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Unexpected error: " + exception.getMessage());
            }
            System.out.println();
        }
        INPUT.close();
    }

    private static void showMenu() {
        System.out.println("1. Add a book");
        System.out.println("2. List all books");
        System.out.println("3. Update book information");
        System.out.println("4. Delete a book");
        System.out.println("5. Search by book ID");
        System.out.println("0. Exit");
    }

    private static void addBook() {
        String title = readText("Title: ");
        String author = readText("Author: ");
        String isbn = readText("ISBN: ");
        int year = readInt("Publication year: ");
        System.out.println("Book added: " + LIBRARY.addBook(title, author, isbn, year));
    }

    private static void listBooks() {
        List<Book> books = LIBRARY.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books have been added yet.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("Total books: " + books.size());
    }

    private static void updateBook() {
        int id = readInt("Enter ID of book to update: ");
        Optional<Book> current = LIBRARY.findById(id);
        if (!current.isPresent()) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println("Current details: " + current.get());
        String title = readText("New title: ");
        String author = readText("New author: ");
        String isbn = readText("New ISBN: ");
        int year = readInt("New publication year: ");
        System.out.println(LIBRARY.updateBook(id, title, author, isbn, year)
                ? "Book updated successfully." : "Book not found.");
    }

    private static void deleteBook() {
        int id = readInt("Enter ID of book to delete: ");
        System.out.println(LIBRARY.deleteBook(id)
                ? "Book deleted successfully." : "Book not found.");
    }

    private static void searchBook() {
        int id = readInt("Enter ID of book to search: ");
        Optional<Book> book = LIBRARY.findById(id);
        System.out.println(book.isPresent() ? book.get().toString() : "Book not found.");
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(INPUT.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
