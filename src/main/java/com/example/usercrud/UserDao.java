package com.example.usercrud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Data-access layer that isolates file storage details. */
public class UserDao {
    public User create(User user) throws IOException {
        List<User> users = Database.loadUsers();
        ensureEmailIsAvailable(users, user.email(), null);
        long nextId = users.stream().map(User::id).max(Long::compareTo).orElse(0L) + 1;
        User createdUser = new User(nextId, user.name(), user.email(), user.age());
        users.add(createdUser);
        Database.saveUsers(users);
        return createdUser;
    }

    public List<User> findAll() throws IOException {
        List<User> users = new ArrayList<>(Database.loadUsers());
        users.sort(Comparator.comparing(User::id));
        return users;
    }

    public Optional<User> findById(long id) throws IOException {
        return Database.loadUsers().stream().filter(user -> user.id() == id).findFirst();
    }

    public boolean update(User user) throws IOException {
        List<User> users = Database.loadUsers();
        for (int index = 0; index < users.size(); index++) {
            if (users.get(index).id().equals(user.id())) {
                ensureEmailIsAvailable(users, user.email(), user.id());
                users.set(index, user);
                Database.saveUsers(users);
                return true;
            }
        }
        return false;
    }

    public boolean delete(long id) throws IOException {
        List<User> users = Database.loadUsers();
        boolean removed = users.removeIf(user -> user.id() == id);
        if (removed) {
            Database.saveUsers(users);
        }
        return removed;
    }

    private void ensureEmailIsAvailable(List<User> users, String email, Long currentUserId) {
        boolean alreadyUsed = users.stream().anyMatch(user ->
                user.email().equalsIgnoreCase(email) && !user.id().equals(currentUserId));
        if (alreadyUsed) {
            throw new IllegalArgumentException("That email already exists.");
        }
    }
}
