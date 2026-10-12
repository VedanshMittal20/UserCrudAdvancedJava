package com.example.usercrud.controller;

import com.example.usercrud.dao.UserDao;
import com.example.usercrud.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                userDao.deleteUser(id);
                response.sendRedirect("users");
                return;
            }

            out.println("<!DOCTYPE html><html><head><title>User CRUD</title></head><body>");
            out.println("<h2>User Management System</h2>");

            User existingUser = null;
            if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                existingUser = userDao.selectUserById(id);
            }

            out.println("<form action='users' method='post'>");
            if (existingUser != null) {
                out.println("<h3>Edit User</h3>");
                out.println("<input type='hidden' name='id' value='" + existingUser.getId() + "'/>");
                out.println("Name: <input type='text' name='name' value='" + existingUser.getName() + "' required/> ");
                out.println("Email: <input type='email' name='email' value='" + existingUser.getEmail() + "' required/> ");
                out.println("<input type='submit' value='Update User'/>");
            } else {
                out.println("<h3>Add New User</h3>");
                out.println("Name: <input type='text' name='name' required/> ");
                out.println("Email: <input type='email' name='email' required/> ");
                out.println("<input type='submit' value='Add User'/>");
            }
            out.println("</form><hr/>");

            List<User> users = userDao.selectAllUsers();
            out.println("<h3>All Users</h3>");
            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");

            if (users.isEmpty()) {
                out.println("<tr><td colspan='4'>No users found in database.</td></tr>");
            } else {
                for (User u : users) {
                    out.println("<tr>");
                    out.println("<td>" + u.getId() + "</td>");
                    out.println("<td>" + u.getName() + "</td>");
                    out.println("<td>" + u.getEmail() + "</td>");
                    out.println("<td>");
                    out.println("<a href='users?action=edit&id=" + u.getId() + "'>Edit</a> | ");
                    out.println("<a href='users?action=delete&id=" + u.getId() + "' onclick='return confirm(\"Are you sure?\");'>Delete</a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        try {
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                User user = new User(id, name, email);
                userDao.updateUser(user);
            } else {
                User newUser = new User(0, name, email);
                userDao.insertUser(newUser);
            }
            response.sendRedirect("users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}