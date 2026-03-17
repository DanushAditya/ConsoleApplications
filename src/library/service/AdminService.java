package library.service;

import library.models.Book;
import library.models.User;
import library.models.BorrowRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AdminService {
    private LibraryData data;

    public AdminService(LibraryData data) {
        this.data = data;
    }

    // Module B: Book Inventory Management
    public void addBook(String isbn, String name, String author, int quantity, double price) {
        if (data.getBooks().containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " already exists. Use modify to update quantity.");
            return;
        }
        data.getBooks().put(isbn, new Book(isbn, name, author, quantity, price));
        System.out.println("Book Added Successfully.");
    }

    public void modifyBookQuantity(String isbn, int newQuantity) {
        Book book = data.getBooks().get(isbn);
        if (book != null) {
            book.setQuantity(newQuantity);
            System.out.println("Book Quantity Updated Successfully.");
        } else {
            System.out.println("Book Not Found.");
        }
    }

    public void deleteBook(String isbn) {
        if (data.getBooks().remove(isbn) != null) {
            System.out.println("Book Deleted Successfully.");
        } else {
            System.out.println("Book Not Found.");
        }
    }

    public void addUser(String email, String password, String name, String role) {
        if (data.getUsers().containsKey(email)) {
            System.out.println("User already exists with this Email.");
            return;
        }
        data.getUsers().put(email, new User(email, password, name, role));
        System.out.println(role.substring(0, 1).toUpperCase() + role.substring(1) + " Added Successfully.");
    }

    public void viewAllBooks(String sortBy) {
        List<Book> bookList = new ArrayList<>(data.getBooks().values());
        if (bookList.isEmpty()) {
            System.out.println("No Books Avavilable in Inventory.");
            return;
        }

        if (sortBy.equalsIgnoreCase("name")) {
            bookList.sort(Comparator.comparing(Book::getName));
        } else if (sortBy.equalsIgnoreCase("quantity")) {
            bookList.sort(Comparator.comparingInt(Book::getQuantity).reversed());
        }

        System.out.println("--- All Books ---");
        for (Book b : bookList) {
            b.display();
        }
    }

    public void searchBook(String query) {
        boolean found = false;
        for (Book b : data.getBooks().values()) {
            if (b.getIsbn().equalsIgnoreCase(query) || b.getName().toLowerCase().contains(query.toLowerCase())) {
                b.display();
                found = true;
            }
        }
        if (!found) System.out.println("No matching books found.");
    }

    // Module E: Reports
    public void reportLowQuantity() {
        System.out.println("--- Books with Less Quantity (< 2) ---");
        for (Book b : data.getBooks().values()) {
            if (b.getQuantity() < 2) b.display();
        }
    }

    public void reportNotBorrowed() {
        System.out.println("--- Books Never Borrowed ---");
        for (Book b : data.getBooks().values()) {
            if (b.getBorrowCount() == 0) b.display();
        }
    }

    public void reportHeavilyBorrowed() {
        System.out.println("--- Heavily Borrowed Books (> 3 times) ---");
        for (Book b : data.getBooks().values()) {
            if (b.getBorrowCount() > 3) b.display();
        }
    }

    public void reportOutstandingStudents() {
        System.out.println("--- Students with Outstanding Books ---");
        for (User u : data.getUsers().values()) {
            if (u.getRole().equals("borrower") && !u.getCurrentBorrows().isEmpty()) {
                u.display();
                for(BorrowRecord rec : u.getCurrentBorrows()) {
                    System.out.print("  -> ");
                    rec.display();
                }
            }
        }
    }

    public void searchBookStatusByIsbn(String isbn) {
        Book b = data.getBooks().get(isbn);
        if (b == null) {
            System.out.println("Book Not Found in Inventory.");
            return;
        }
        b.display();
        System.out.println("Current Borrowers:");
        boolean borrowed = false;
        for (User u : data.getUsers().values()) {
            if (u.getRole().equals("borrower")) {
                for (BorrowRecord rec : u.getCurrentBorrows()) {
                    if (rec.getBookIsbn().equals(isbn)) {
                        System.out.println(" - " + u.getName() + " (" + u.getEmail() + "), Expected Return: " + rec.getExpectedReturnDate());
                        borrowed = true;
                    }
                }
            }
        }
        if (!borrowed) System.out.println(" Currently not borrowed by anyone.");
    }
}
