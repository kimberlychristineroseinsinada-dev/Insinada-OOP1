import java.io.*;
import java.util.*;
import java.time.LocalDate;

// Custom Exception
class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}

// Base Class
class Person {
    protected String id;
    protected String name;
    
    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name);
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
}

// User Class
class User extends Person {
    private String password;
    private String role;
    
    public User(String id, String name, String password, String role) {
        super(id, name);
        this.password = password;
        this.role = role;
    }
    
    public void displayInfo() {
        System.out.println("User ID: " + id + ", Name: " + name + ", Role: " + role);
    }
    
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

// Book Class
class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean available;
    
    public Book(String bookId, String title, String author, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = available;
    }
    
    public void displayInfo() {
        System.out.println("Book ID: " + bookId + ", Title: " + title + 
                         ", Author: " + author + ", Available: " + (available ? "Yes" : "No"));
    }
    
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

// Transaction Class
class Transaction {
    private String transactionId;
    private String userId;
    private String bookId;
    private String dateBorrowed;
    private String dateReturned;
    
    public Transaction(String transactionId, String userId, String bookId, String dateBorrowed, String dateReturned) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }
    
    public void displayInfo() {
        System.out.println("Transaction ID: " + transactionId + ", User: " + userId + 
                         ", Book: " + bookId + ", Borrowed: " + dateBorrowed + 
                         ", Returned: " + (dateReturned.equals("null") ? "Not yet" : dateReturned));
    }
    
    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public String getDateBorrowed() { return dateBorrowed; }
    public String getDateReturned() { return dateReturned; }
    public void setDateReturned(String date) { this.dateReturned = date; }
}

// Main Library System
public class LibraryManagementSystem {
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private User loggedInUser;
    private Scanner sc = new Scanner(System.in);
    
    // Load data from files
    public void loadData() {
        loadUsers();
        loadBooks();
        loadTransactions();
    }
    
    // Load users from file
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    users.add(new User(p[0], p[1], p[2], p[3]));
                }
            }
            System.out.println("Users loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("users.txt not found. Creating new file with default data.");
            createDefaultUsers();
        } catch (IOException e) {
            System.out.println("Error reading users.txt");
        }
    }
    
    // Load books from file
    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    books.add(new Book(p[0], p[1], p[2], Boolean.parseBoolean(p[3])));
                }
            }
            System.out.println("Books loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("books.txt not found. Creating new file with default data.");
            createDefaultBooks();
        } catch (IOException e) {
            System.out.println("Error reading books.txt");
        }
    }
    
    // Load transactions from file
    private void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 5) {
                    transactions.add(new Transaction(p[0], p[1], p[2], p[3], p[4]));
                }
            }
            System.out.println("Transactions loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("transactions.txt not found. Creating new empty file.");
        } catch (IOException e) {
            System.out.println("Error reading transactions.txt");
        }
    }
    
    // Create default users if file doesn't exist
    private void createDefaultUsers() {
        users.add(new User("U001", "John Doe", "pass123", "user"));
        users.add(new User("U002", "Jane Smith", "abc123", "user"));
        users.add(new User("A001", "Admin", "admin123", "admin"));
        saveUsers();
    }
    
    // Create default books if file doesn't exist
    private void createDefaultBooks() {
        books.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", true));
        books.add(new Book("B002", "To Kill a Mockingbird", "Harper Lee", true));
        books.add(new Book("B003", "1984", "George Orwell", true));
        saveBooks();
    }
    
    // Save users to file
    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt"))) {
            for (User u : users) {
                pw.println(u.getId() + "," + u.getName() + "," + u.getPassword() + "," + u.getRole());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    // Save books to file
    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                pw.println(b.getBookId() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }
    
    // Save transactions to file
    private void saveTransactions() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("transactions.txt"))) {
            for (Transaction t : transactions) {
                pw.println(t.getTransactionId() + "," + t.getUserId() + "," + t.getBookId() + "," + t.getDateBorrowed() + "," + t.getDateReturned());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    // Login
    public boolean login() {
        for (int i = 0; i < 3; i++) {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            
            for (User user : users) {
                if (user.getName().equals(username) && user.getPassword().equals(password)) {
                    loggedInUser = user;
                    System.out.println("\nWelcome, " + user.getName() + "!");
                    
                    // Polymorphism demo
                    Person p = user;
                    p.displayInfo();
                    return true;
                }
            }
            System.out.println("Invalid login. Attempts left: " + (2 - i));
        }
        return false;
    }
    
    // Main menu
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== LIBRARY MENU ===");
            System.out.println("1. View Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            if (loggedInUser.getRole().equals("admin")) {
                System.out.println("4. View Transactions");
            }
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            
            int choice = Integer.parseInt(sc.nextLine());
            
            if (choice == 1) viewBooks();
            else if (choice == 2) borrowBook();
            else if (choice == 3) returnBook();
            else if (choice == 4 && loggedInUser.getRole().equals("admin")) viewTransactions();
            else if (choice == 0) {
                saveUsers();
                saveBooks();
                saveTransactions();
                return;
            }
        }
    }
    
    // View books
    private void viewBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        for (Book b : books) {
            b.displayInfo();
        }
    }
    
    // Borrow book
    private void borrowBook() {
        try {
            System.out.print("Enter Book ID: ");
            String id = sc.nextLine();
            
            Book book = null;
            for (Book b : books) {
                if (b.getBookId().equals(id)) {
                    book = b;
                    break;
                }
            }
            
            if (book == null) {
                throw new NullPointerException("Book not found");
            }
            
            if (!book.isAvailable()) {
                throw new BookUnavailableException("Book unavailable");
            }
            
            book.setAvailable(false);
            String tId = "T" + (transactions.size() + 1);
            transactions.add(new Transaction(tId, loggedInUser.getId(), id, LocalDate.now().toString(), "null"));
            System.out.println("Book borrowed!");
            
            saveBooks();
            saveTransactions();
            
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (BookUnavailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Return book
    private void returnBook() {
        try {
            System.out.print("Enter Book ID: ");
            String id = sc.nextLine();
            
            Book book = null;
            for (Book b : books) {
                if (b.getBookId().equals(id)) {
                    book = b;
                    break;
                }
            }
            
            if (book == null) {
                throw new NullPointerException("Book not found");
            }
            
            book.setAvailable(true);
            
            for (Transaction t : transactions) {
                if (t.getUserId().equals(loggedInUser.getId()) && 
                    t.getBookId().equals(id) && 
                    t.getDateReturned().equals("null")) {
                    t.setDateReturned(LocalDate.now().toString());
                    break;
                }
            }
            
            System.out.println("Book returned!");
            
            saveBooks();
            saveTransactions();
            
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // View transactions (admin only)
    private void viewTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        for (Transaction t : transactions) {
            t.displayInfo();
        }
    }
    
    // Main
    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();
        
        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        system.loadData();
        
        if (system.login()) {
            system.displayMenu();
        }
        
        System.out.println("Goodbye!");
    }
}