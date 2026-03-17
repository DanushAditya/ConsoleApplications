package library.service;

import library.models.Book;
import library.models.User;

import java.util.HashMap;

public class LibraryData {
    private HashMap<String, Book> books;
    private HashMap<String, User> users; // key: email

    public LibraryData() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
        
        // Seed default Admin
        User admin = new User("admin@lib.com", "admin123", "Chief Librarian", "admin");
        users.put(admin.getEmail(), admin);
        
        // Seed a sample borrower
        User borrower = new User("student@lib.com", "pass123", "Alice", "borrower");
        users.put(borrower.getEmail(), borrower);
        
        // Seed sample books
        books.put("1001", new Book("1001", "Java Programming", "James Gosling", 5, 800.0));
        books.put("1002", new Book("1002", "Data Structures", "Robert Sedgewick", 3, 1200.0));
        books.put("1003", new Book("1003", "Clean Code", "Robert Martin", 0, 950.0));
    }

    public HashMap<String, Book> getBooks() {
        return books;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}
