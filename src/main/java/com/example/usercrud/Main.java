package com.example.usercrud;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public static void main(String[] args) {
        try {
            Database.initialize();
            new Main().start();
        } catch (SQLException exception) {
            System.err.println("Database error: " + exception.getMessage());
        }
    }

    private void start() {
        while (true) {
            printMenu();
            switch (ask("Choose an option: ")) {
                case "1" -> createUser();
                case "2" -> listUsers();
                case "3" -> findUser();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> { System.out.println("Goodbye."); return; }
                default -> System.out.println("Please choose a valid option.");
            }
        }
    }

    private void createUser() {
        try {
            User user = userDao.create(readUser(null));
            System.out.println("Created user with ID " + user.id() + ".");
        } catch (SQLException exception) { printDatabaseIssue(exception); }
    }

    private void listUsers() {
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) { System.out.println("No users found."); return; }
            System.out.println("ID | Name                 | Email                          | Age");
            System.out.println("---+----------------------+--------------------------------+----");
            users.forEach(System.out::println);
        } catch (SQLException exception) { printDatabaseIssue(exception); }
    }

    private void findUser() {
        try {
            long id = readId();
            System.out.println(userDao.findById(id).map(User::toString).orElse("User not found."));
        } catch (SQLException exception) { printDatabaseIssue(exception); }
    }

    private void updateUser() {
        try {
            long id = readId();
            User existing = userDao.findById(id).orElse(null);
            if (existing == null) { System.out.println("User not found."); return; }
            User updated = readUser(existing);
            System.out.println(userDao.update(updated) ? "User updated." : "User not found.");
        } catch (SQLException exception) { printDatabaseIssue(exception); }
    }

    private void deleteUser() {
        try {
            long id = readId();
            String confirm = ask("Type YES to delete: ");
            if (!"YES".equals(confirm)) { System.out.println("Delete cancelled."); return; }
            System.out.println(userDao.delete(id) ? "User deleted." : "User not found.");
        } catch (SQLException exception) { printDatabaseIssue(exception); }
    }

    private User readUser(User existing) {
        String name = readRequired(existing == null ? "Name: " : "Name [" + existing.name() + "]: ", existing == null ? null : existing.name());
        String email;
        do { email = readRequired(existing == null ? "Email: " : "Email [" + existing.email() + "]: ", existing == null ? null : existing.email());
            if (!EMAIL.matcher(email).matches()) System.out.println("Enter a valid email address.");
        } while (!EMAIL.matcher(email).matches());
        int age = readAge(existing == null ? "Age: " : "Age [" + existing.age() + "]: ", existing == null ? null : existing.age());
        return new User(existing == null ? null : existing.id(), name, email, age);
    }

    private long readId() { while (true) try { long id = Long.parseLong(ask("User ID: ")); if (id > 0) return id; } catch (NumberFormatException ignored) { } System.out.println("Enter a positive numeric ID."); }
    private int readAge(String prompt, Integer fallback) { while (true) try { String value = ask(prompt); if (value.isBlank() && fallback != null) return fallback; int age = Integer.parseInt(value); if (age >= 1 && age <= 150) return age; } catch (NumberFormatException ignored) { } System.out.println("Age must be between 1 and 150."); }
    private String readRequired(String prompt, String fallback) { while (true) { String value = ask(prompt); if (value.isBlank() && fallback != null) return fallback; if (!value.isBlank()) return value; System.out.println("This field is required."); } }
    private String ask(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private void printDatabaseIssue(SQLException exception) { System.out.println("Could not complete the request. " + (exception.getErrorCode() == 23505 ? "That email already exists." : exception.getMessage())); }
    private void printMenu() { System.out.println("\n=== User Management ===\n1. Create user\n2. List users\n3. Find user\n4. Update user\n5. Delete user\n0. Exit"); }
}
