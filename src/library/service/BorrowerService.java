package library.service;

import library.models.Book;
import library.models.User;
import library.models.BorrowRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class BorrowerService {
    private LibraryData data;
    private Scanner sc = new Scanner(System.in);
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BorrowerService(LibraryData data) {
        this.data = data;
    }

    public void viewAllBooks() {
        System.out.println("--- Available Books ---");
        for (Book b : data.getBooks().values()) {
            if (b.getQuantity() > 0) {
                b.display();
            }
        }
    }

    public void checkoutBook(User borrower, String isbn) {
        if (borrower.getSecurityDeposit() < 500) {
            System.out.println("Cannot borrow. Minimum security deposit of Rs 500 must be maintained.");
            return;
        }
        if (borrower.getCurrentBorrows().size() >= 3) {
            System.out.println("Cannot borrow. Maximum of 3 books allowed.");
            return;
        }

        // Check if book exists and available
        Book book = data.getBooks().get(isbn);
        if (book == null || book.getQuantity() == 0) {
            System.out.println("Book not found or out of stock.");
            return;
        }

        // Check duplicate concurrent borrow
        for (BorrowRecord rec : borrower.getCurrentBorrows()) {
            if (rec.getBookIsbn().equals(isbn)) {
                System.out.println("You have already borrowed this book.");
                return;
            }
        }

        // Processing borrowing date
        System.out.print("Enter current date (dd/MM/yyyy): ");
        String currentDateStr = sc.nextLine();
        LocalDate borrowDate = LocalDate.parse(currentDateStr, dtf);
        LocalDate returnDate = borrowDate.plusDays(15);

        BorrowRecord rec = new BorrowRecord(isbn, book.getName(), dtf.format(borrowDate), dtf.format(returnDate));
        borrower.addCurrentBorrow(rec);
        book.decreaseQuantity();
        book.incrementBorrowCount();

        System.out.println("Book Checked out Successfully! Due Date: " + rec.getExpectedReturnDate());
    }

    public void returnBook(User borrower) {
        System.out.println("--- Your Borrows ---");
        for (int i = 0; i < borrower.getCurrentBorrows().size(); i++) {
            System.out.print((i + 1) + ". ");
            borrower.getCurrentBorrows().get(i).display();
        }
        System.out.print("Select book number to return (or 0 to cancel): ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice == 0 || choice > borrower.getCurrentBorrows().size()) return;

        BorrowRecord rec = borrower.getCurrentBorrows().get(choice - 1);
        System.out.print("Enter actual return date (dd/MM/yyyy): ");
        LocalDate returnDateStr = LocalDate.parse(sc.nextLine(), dtf);
        LocalDate expectedDate = LocalDate.parse(rec.getExpectedReturnDate(), dtf);

        Book book = data.getBooks().get(rec.getBookIsbn());
        double fine = 0.0;

        // Calculate fine if delay
        if (returnDateStr.isAfter(expectedDate)) {
            long delayedDays = ChronoUnit.DAYS.between(expectedDate, returnDateStr);
            if(delayedDays <= 10) {
                fine = delayedDays * 2;
            } else {
                long chunks = delayedDays / 10;
                fine = 20 * Math.pow(2, chunks - 1); // Exponential calculation as a simple example
            }
            
            double maxFine = book.getPrice() * 0.8;
            fine = Math.min(fine, maxFine);

            System.out.println("Delayed return by " + delayedDays + " days. Fine Calculated: Rs " + String.format("%.2f", fine));
            handleFinePayment(borrower, fine, "Delayed Return Fine for " + book.getName());
        }

        book.increaseQuantity();
        borrower.removeCurrentBorrow(rec);
        borrower.addPastBorrow(rec);
        System.out.println("Book Returned successfully.");
    }

    public void extendTenure(User borrower) {
        System.out.println("--- Your Borrows ---");
        for (int i = 0; i < borrower.getCurrentBorrows().size(); i++) {
            System.out.print((i + 1) + ". ");
            borrower.getCurrentBorrows().get(i).display();
        }
        System.out.print("Select book number to extend tenure (or 0 to cancel): ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice == 0 || choice > borrower.getCurrentBorrows().size()) return;

        BorrowRecord rec = borrower.getCurrentBorrows().get(choice - 1);
        if (rec.getExtensions() >= 2) {
            System.out.println("Cannot extend further. Maximum 2 consecutive extensions allowed.");
            return;
        }

        LocalDate currDue = LocalDate.parse(rec.getExpectedReturnDate(), dtf);
        LocalDate newDue = currDue.plusDays(15);
        rec.setExpectedReturnDate(dtf.format(newDue));
        rec.incrementExtensions();
        System.out.println("Tenure extended successfully. New Due Date: " + rec.getExpectedReturnDate());
    }

    public void reportLostBook(User borrower) {
        System.out.println("--- Your Borrows ---");
        for (int i = 0; i < borrower.getCurrentBorrows().size(); i++) {
            System.out.print((i + 1) + ". ");
            borrower.getCurrentBorrows().get(i).display();
        }
        System.out.print("Select book number lost (or 0 to cancel): ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice == 0 || choice > borrower.getCurrentBorrows().size()) return;

        BorrowRecord rec = borrower.getCurrentBorrows().get(choice - 1);
        Book book = data.getBooks().get(rec.getBookIsbn());
        double fine = book.getPrice() * 0.5;

        System.out.println("Book '" + book.getName() + "' reported lost.");
        System.out.println("Fine Calculated (50% of cost): Rs " + String.format("%.2f", fine));
        handleFinePayment(borrower, fine, "Lost Book Fine for " + book.getName());

        borrower.removeCurrentBorrow(rec);
        // Note: Book quantity is not increased back as it's lost
    }

    public void reportLostCard(User borrower) {
        double fine = 10.0;
        System.out.println("Membership card reported lost.");
        System.out.println("Placing replacement request. Fine Calculated: Rs " + fine);
        handleFinePayment(borrower, fine, "Lost Membership Card");
    }

    private void handleFinePayment(User borrower, double fine, String reason) {
        System.out.print("Pay fine by (1) Cash or (2) Deduct from Security Deposit (Deposit: " + borrower.getSecurityDeposit() + ")? ");
        int opt = Integer.parseInt(sc.nextLine());
        if (opt == 2) {
            if (borrower.getSecurityDeposit() >= fine) {
                borrower.deductDeposit(fine);
                System.out.println("Fine deducted from security deposit. Remaining deposit: " + borrower.getSecurityDeposit());
            } else {
                System.out.println("Insufficient deposit. Please pay by cash.");
                opt = 1;
            }
        }
        if(opt == 1) {
            System.out.println("Please pay Rs " + String.format("%.2f", fine) + " by Cash at the counter.");
        }
        borrower.addFineHistory(reason + " - Rs " + String.format("%.2f", fine));
    }

    public void viewFineHistory(User borrower) {
        System.out.println("--- Fine History (" + borrower.getName() + ") ---");
        if (borrower.getFineHistory().isEmpty()) {
            System.out.println("No past fines.");
        } else {
            for (String f : borrower.getFineHistory()) {
                System.out.println(" - " + f);
            }
        }
    }

    public void viewBorrowHistory(User borrower) {
        System.out.println("--- Borrow History (" + borrower.getName() + ") ---");
        if (borrower.getPastBorrows().isEmpty()) {
            System.out.println("No past borrows.");
        } else {
            for (BorrowRecord f : borrower.getPastBorrows()) {
                f.display();
            }
        }
    }
}
