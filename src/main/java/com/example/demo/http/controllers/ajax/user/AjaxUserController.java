package com.example.demo.http.controllers.ajax.user;

import com.example.demo.http.controllers.ajax.BaseAjaxController;
import com.example.demo.models.User;
import com.example.demo.services.implementations.user.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/ajax/users/*")
@MultipartConfig
public class AjaxUserController extends BaseAjaxController {

    @Inject
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] segments = parsePathSegments(request);
        String action = getAction(segments, "create");
        String id = getId(segments);

        switch (action) {
            case "create":
                handleCreateUser(request, response);
                break;
            case "update":
                handleUpdateUser(request, response, id);
                break;
            case "delete":
                handleDeleteUser(request, response, id);
                break;
            case "activate":
                handleActivateUser(request, response, id);
                break;
            case "deactivate":
                handleDeactivateUser(request, response, id);
                break;
            default:
                error("Invalid action", 400, null, null, response);
                break;
        }
    }

    private void handleCreateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String status = request.getParameter("status");

            if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                error("Username, email, and password are required", 400, null, null, response);
                return;
            }

            User user = new User(username, email, password, status != null ? status : "ACTIVE");
            var savedUser = userService.createUser(user);

            if (savedUser.isPresent()) {
                Map<String, Object> data = Map.of(
                    "id", savedUser.get().getId(),
                    "username", savedUser.get().getUsername(),
                    "email", savedUser.get().getEmail(),
                    "status", savedUser.get().getStatus()
                );
                success("User created successfully", 200, data, null, response);
            } else {
                error("Failed to create user", 500, null, null, response);
            }
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 400, null, null, response);
        } catch (Exception e) {
            e.printStackTrace();
            error("Error creating user: " + e.getMessage(), 500, null, null, response);
        }
    }

    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            error("User ID is required", 400, null, null, response);
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            var userOpt = userService.findById(userId);

            if (userOpt.isEmpty()) {
                error("User not found", 404, null, null, response);
                return;
            }

            User user = userOpt.get();
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String status = request.getParameter("status");

            // Validate required fields
            if (username == null || username.trim().isEmpty()) {
                error("Username is required", 400, null, null, response);
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                error("Email is required", 400, null, null, response);
                return;
            }

            // Update fields
            user.setUsername(username);
            user.setEmail(email);
            
            // Only update password if a new one is provided
            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(password);
            }
            
            if (status != null) {
                user.setStatus(status);
            }

            var updated = userService.updateUser(user);

            if (updated.isPresent() && updated.get()) {
                Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "status", user.getStatus()
                );
                success("User updated successfully", 200, data, null, response);
            } else {
                error("Failed to update user", 500, null, null, response);
            }
        } catch (NumberFormatException e) {
            error("Invalid user ID", 400, null, null, response);
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 400, null, null, response);
        } catch (Exception e) {
            error("Error updating user: " + e.getMessage(), 500, null, null, response);
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            error("User ID is required", 400, null, null, response);
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            var deleted = userService.deleteUser(userId);

            if (deleted.isPresent() && deleted.get()) {
                Map<String, Object> data = Map.of("id", userId);
                success("User deleted successfully", 200, data, null, response);
            } else {
                error("Failed to delete user", 500, null, null, response);
            }
        } catch (NumberFormatException e) {
            error("Invalid user ID", 400, null, null, response);
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 400, null, null, response);
        } catch (Exception e) {
            error("Error deleting user: " + e.getMessage(), 500, null, null, response);
        }
    }

    private void handleActivateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            error("User ID is required", 400, null, null, response);
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            var userOpt = userService.findById(userId);
            
            if (userOpt.isEmpty()) {
                error("User not found", 404, null, null, response);
                return;
            }
            
            var activated = userService.activateUser(userId);

            if (activated.isPresent() && activated.get()) {
                User user = userOpt.get();
                Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "status", "ACTIVE"
                );
                success("User activated successfully", 200, data, null, response);
            } else {
                error("Failed to activate user", 500, null, null, response);
            }
        } catch (NumberFormatException e) {
            error("Invalid user ID", 400, null, null, response);
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 400, null, null, response);
        } catch (Exception e) {
            error("Error activating user: " + e.getMessage(), 500, null, null, response);
        }
    }

    private void handleDeactivateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            error("User ID is required", 400, null, null, response);
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            var userOpt = userService.findById(userId);
            
            if (userOpt.isEmpty()) {
                error("User not found", 404, null, null, response);
                return;
            }
            
            var deactivated = userService.deactivateUser(userId);

            if (deactivated.isPresent() && deactivated.get()) {
                User user = userOpt.get();
                Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "status", "INACTIVE"
                );
                success("User deactivated successfully", 200, data, null, response);
            } else {
                error("Failed to deactivate user", 500, null, null, response);
            }
        } catch (NumberFormatException e) {
            error("Invalid user ID", 400, null, null, response);
        } catch (IllegalArgumentException e) {
            error(e.getMessage(), 400, null, null, response);
        } catch (Exception e) {
            error("Error deactivating user: " + e.getMessage(), 500, null, null, response);
        }
    }
}

