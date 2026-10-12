package com.example.usercrud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Data-access layer that isolates all JDBC details. */
public class UserDao {
    public User create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindUser(statement, user);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) return new User(keys.getLong(1), user.name(), user.email(), user.age());
            }
        }
        throw new SQLException("Could not create user.");
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id, name, email, age FROM users ORDER BY id");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) users.add(map(result));
        }
        return users;
    }

    public Optional<User> findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, age FROM users WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        }
    }

    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            bindUser(statement, user);
            statement.setLong(4, user.id());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(long id) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    private void bindUser(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.name());
        statement.setString(2, user.email());
        statement.setInt(3, user.age());
    }

    private User map(ResultSet result) throws SQLException {
        return new User(result.getLong("id"), result.getString("name"), result.getString("email"), result.getInt("age"));
    }
}
