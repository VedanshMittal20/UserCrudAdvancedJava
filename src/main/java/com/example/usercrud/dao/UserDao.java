package com.example.usercrud.dao;

import com.example.usercrud.config.Database;
import com.example.usercrud.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Fetch all users (READ)
    public List<User> selectAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                users.add(new User(id, name, email));
            }
        }
        return users;
    }

    // Fetch user by ID (READ single)
    public User selectUserById(int id) throws Exception {
        User user = null;
        String sql = "SELECT id, name, email FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    user = new User(id, name, email);
                }
            }
        }
        return user;
    }

    // Insert user (CREATE)
    public void insertUser(User user) throws Exception {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();
        }
    }

    // Update user (UPDATE)
    public boolean updateUser(User user) throws Exception {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        boolean rowUpdated;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getId());
            rowUpdated = stmt.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Delete user (DELETE)
    public boolean deleteUser(int id) throws Exception {
        String sql = "DELETE FROM users WHERE id = ?";
        boolean rowDeleted;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            rowDeleted = stmt.executeUpdate() > 0;
        }
        return rowDeleted;
    }
}