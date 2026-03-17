package library.models;

public class BorrowRecord {
    private String bookIsbn;
    private String bookName;
    private String borrowDate;
    private String expectedReturnDate;
    private int extensions;

    public BorrowRecord(String bookIsbn, String bookName, String borrowDate, String expectedReturnDate) {
        this.bookIsbn = bookIsbn;
        this.bookName = bookName;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.extensions = 0;
    }

    public String getBookIsbn() { return bookIsbn; }
    public String getBookName() { return bookName; }
    public String getBorrowDate() { return borrowDate; }
    public String getExpectedReturnDate() { return expectedReturnDate; }
    public int getExtensions() { return extensions; }

    public void setExpectedReturnDate(String expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
    public void incrementExtensions() { this.extensions++; }
    
    public void display() {
        System.out.printf("ISBN: %-10s | Title: %-20s | Borrowed: %-10s | Due: %-10s | Extensions: %d\n", 
                          bookIsbn, bookName, borrowDate, expectedReturnDate, extensions);
    }
}
