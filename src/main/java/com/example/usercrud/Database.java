package com.example.usercrud;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Handles persistent storage using only core Java file APIs. */
public final class Database {
    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("users.dat");

    private Database() { }

    public static void initialize() throws IOException {
        Files.createDirectories(DATA_DIRECTORY);
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() throws IOException {
        if (Files.notExists(DATA_FILE)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(DATA_FILE))) {
            return new ArrayList<>((List<User>) input.readObject());
        } catch (ClassNotFoundException exception) {
            throw new IOException("Saved user data cannot be read.", exception);
        }
    }

    public static void saveUsers(List<User> users) throws IOException {
        Path temporaryFile = Files.createTempFile(DATA_DIRECTORY, "users-", ".tmp");
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(temporaryFile))) {
            output.writeObject(users);
        }
        Files.move(temporaryFile, DATA_FILE, StandardCopyOption.REPLACE_EXISTING);
    }
}
