package com.example.demo.http.controllers.web.user;

import com.example.demo.models.User;
import com.example.demo.services.implementations.UserService;
import com.example.demo.http.controllers.web.BaseWebController;
import com.example.demo.http.pages.Page;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/users")
public class UserController extends BaseWebController {
    
    @Inject
    private UserService userService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action != null ? action : "list") {
            case "list":
                handleListUsers(request, response);
                break;
            case "view":
                handleViewUser(request, response);
                break;
            case "create":
                handleCreateForm(request, response);
                break;
            case "edit":
                handleEditForm(request, response);
                break;
            default:
                handleListUsers(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action != null ? action : "create") {
            case "create":
                handleCreateUser(request, response);
                break;
            case "update":
                handleUpdateUser(request, response);
                break;
            case "delete":
                handleDeleteUser(request, response);
                break;
            case "activate":
                handleActivateUser(request, response);
                break;
            case "deactivate":
                handleDeactivateUser(request, response);
                break;
            default:
                handleListUsers(request, response);
                break;
        }
    }
    
    private void handleListUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Optional<List<User>> usersOpt = userService.findAll();
            List<User> users = usersOpt.orElse(List.of());
            
            request.setAttribute("users", users);
            request.setAttribute("pageTitle", "User Management");
            
            renderPage(request, response, new Page("users/list", "User List"));
            
        } catch (Exception e) {
            handleError(request, response, "Error loading users: " + e.getMessage());
        }
    }
    
    private void handleViewUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/users");
                return;
            }
            
            Long userId = Long.parseLong(idParam);
            Optional<User> userOpt = userService.findById(userId);
            
            if (userOpt.isPresent()) {
                request.setAttribute("user", userOpt.get());
                request.setAttribute("pageTitle", "User Details");
                renderPage(request, response, new Page("users/view", "User Details"));
            } else {
                handleError(request, response, "User not found");
            }
            
        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid user ID");
        } catch (Exception e) {
            handleError(request, response, "Error loading user: " + e.getMessage());
        }
    }
    
    private void handleCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setAttribute("pageTitle", "Create User");
        renderPage(request, response, new Page("users/create", "Create User"));
    }
    
    private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String status = request.getParameter("status");
            
            User user = new User(username, email, password, status);
            Optional<User> savedUser = userService.save(user);
            
            if (savedUser.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/users?action=view&id=" + savedUser.get().getId());
            } else {
                handleError(request, response, "Failed to create user");
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            handleCreateForm(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error creating user: " + e.getMessage());
        }
    }
    
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            Long userId = Long.parseLong(idParam);
            Optional<User> userOpt = userService.findById(userId);
            
            if (userOpt.isPresent()) {
                request.setAttribute("user", userOpt.get());
                request.setAttribute("pageTitle", "Edit User");
                renderPage(request, response, new Page("users/edit", "Edit User"));
            } else {
                handleError(request, response, "User not found");
            }
            
        } catch (Exception e) {
            handleError(request, response, "Error loading user for edit: " + e.getMessage());
        }
    }
    
    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            Long userId = Long.parseLong(idParam);
            
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isEmpty()) {
                handleError(request, response, "User not found");
                return;
            }
            
            User user = userOpt.get();
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));
            user.setStatus(request.getParameter("status"));
            
            Optional<Boolean> updated = userService.update(user);
            
            if (updated.isPresent() && updated.get()) {
                response.sendRedirect(request.getContextPath() + "/users?action=view&id=" + userId);
            } else {
                handleError(request, response, "Failed to update user");
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            handleEditForm(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error updating user: " + e.getMessage());
        }
    }
    
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            Long userId = Long.parseLong(idParam);
            
            Optional<Boolean> deleted = userService.deleteById(userId);
            
            if (deleted.isPresent() && deleted.get()) {
                response.sendRedirect(request.getContextPath() + "/users");
            } else {
                handleError(request, response, "Failed to delete user");
            }
            
        } catch (Exception e) {
            handleError(request, response, "Error deleting user: " + e.getMessage());
        }
    }
    
    private void handleActivateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            Long userId = Long.parseLong(idParam);
            
            Optional<Boolean> activated = userService.activateUser(userId);
            
            if (activated.isPresent() && activated.get()) {
                response.sendRedirect(request.getContextPath() + "/users?action=view&id=" + userId);
            } else {
                handleError(request, response, "Failed to activate user");
            }
            
        } catch (Exception e) {
            handleError(request, response, "Error activating user: " + e.getMessage());
        }
    }
    
    private void handleDeactivateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idParam = request.getParameter("id");
            Long userId = Long.parseLong(idParam);
            
            Optional<Boolean> deactivated = userService.deactivateUser(userId);
            
            if (deactivated.isPresent() && deactivated.get()) {
                response.sendRedirect(request.getContextPath() + "/users?action=view&id=" + userId);
            } else {
                handleError(request, response, "Failed to deactivate user");
            }
            
        } catch (Exception e) {
            handleError(request, response, "Error deactivating user: " + e.getMessage());
        }
    }
}
