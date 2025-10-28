package com.nphuonha.servletkit.http.controllers.web.user;

import com.nphuonha.servletkit.models.User;
import com.nphuonha.servletkit.http.controllers.web.BaseWebController;
import com.nphuonha.servletkit.http.pages.Page;

import com.nphuonha.servletkit.services.interfaces.user.IUserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/users/*")
public class UserController extends BaseWebController {

    @Inject
    private IUserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] segments = parsePathSegments(request);
        String action = getAction(segments, "list");
        String id = getId(segments);

        switch (action) {
            case "list":
                handleListUsers(request, response);
                break;
            case "view":
                handleViewUser(request, response, id);
                break;
            case "create":
                handleCreateForm(request, response);
                break;
            case "edit":
                handleEditForm(request, response, id);
                break;
            default:
                handleListUsers(request, response);
                break;
        }
    }

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
                handleListUsers(request, response);
                break;
        }
    }

    private void handleListUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Optional<List<User>> usersOpt = userService.findAll();
            List<User> users = usersOpt.orElse(List.of());

            System.out.println("DEBUG - Users: " + users);

            request.setAttribute("users", users);
            request.setAttribute("pageTitle", "User Management");
            String[] scripts = new String[]{"user-management.js"};
            Page page = new Page().setTitle("User List").setPageName("users/list").setLayout("app").setScripts(scripts);
            renderPage(request, response, page);
        } catch (Exception e) {
            handleError(request, response, "Error loading users: " + e.getMessage());
        }
    }

    private void handleViewUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        
        if (!isValidId(id)) {
            redirect(request, response, "/users");
            return;
        }

        Long userId = parseId(id);
        if (userId == null) {
            redirect(request, response, "/users");
            return;
        }

        try {
            Optional<User> userOpt = userService.findById(userId);

            if (userOpt.isPresent()) {
                request.setAttribute("user", userOpt.get());
                request.setAttribute("pageTitle", "User Details");

                String[] scripts = new String[]{"user-management.js"};
                Page page = new Page().setTitle("User Details").setPageName("users/view").setLayout("app").setScripts(scripts);
                renderPage(request, response, page);
            } else {
                redirect(request, response, "/users");
            }
        } catch (Exception e) {
            handleError(request, response, "Error loading user: " + e.getMessage());
        }
    }

    private void handleCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("pageTitle", "Create User");
        String[] scripts = new String[]{"user-management.js"};
        Page page = new Page().setTitle("Create User").setPageName("users/create").setLayout("app").setScripts(scripts);
        renderPage(request, response, page);
    }

    private void handleCreateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String username = getParameter(request, "username", "");
            String email = getParameter(request, "email", "");
            String password = getParameter(request, "password", "");
            String status = getParameter(request, "status", "ACTIVE");

            User user = new User(username, email, password, status);
            Optional<User> savedUser = userService.createUser(user);

            if (savedUser.isPresent()) {
                setSuccessMessage(request, "User created successfully");
                redirect(request, response, "/users/view/" + savedUser.get().getId());
            } else {
                setErrorMessage(request, "Failed to create user");
                redirect(request, response, "/users/create");
            }
        } catch (Exception e) {
            setErrorMessage(request, "Error creating user: " + e.getMessage());
            redirect(request, response, "/users/create");
        }
    }

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            Optional<User> userOpt = userService.findById(userId);

            if (userOpt.isPresent()) {
                request.setAttribute("user", userOpt.get());
                request.setAttribute("pageTitle", "Edit User");
                String[] scripts = new String[]{"user-management.js"};
                Page page = new Page().setTitle("Edit User").setPageName("users/edit").setLayout("app").setScripts(scripts);
                page.render(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/users");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        }
    }

    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }

        try {
            Long userId = Long.parseLong(id);
            Optional<User> userOpt = userService.findById(userId);
            
            if (userOpt.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/users");
                return;
            }

            User user = userOpt.get();
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));
            user.setStatus(request.getParameter("status"));

            Optional<Boolean> updated = userService.updateUser(user);

            if (updated.isPresent() && updated.get()) {
                response.sendRedirect(request.getContextPath() + "/users/view/" + userId);
            } else {
                response.sendRedirect(request.getContextPath() + "/users/edit/" + userId);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/users/edit/" + id);
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (!isValidId(id)) {
            redirect(request, response, "/users");
            return;
        }

        Long userId = parseId(id);
        if (userId == null) {
            redirect(request, response, "/users");
            return;
        }

        try {
            Optional<Boolean> deleted = userService.deleteUser(userId);

            if (deleted.isPresent() && deleted.get()) {
                setSuccessMessage(request, "User deleted successfully");
                redirect(request, response, "/users");
            } else {
                setErrorMessage(request, "Failed to delete user");
                redirect(request, response, "/users");
            }
        } catch (Exception e) {
            setErrorMessage(request, "Error deleting user: " + e.getMessage());
            redirect(request, response, "/users");
        }
    }

    private void handleActivateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (!isValidId(id)) {
            redirect(request, response, "/users");
            return;
        }

        Long userId = parseId(id);
        if (userId == null) {
            redirect(request, response, "/users");
            return;
        }

        try {
            Optional<Boolean> activated = userService.activateUser(userId);

            if (activated.isPresent() && activated.get()) {
                setSuccessMessage(request, "User activated successfully");
                redirect(request, response, "/users/view/" + userId);
            } else {
                setErrorMessage(request, "Failed to activate user");
                redirect(request, response, "/users/view/" + userId);
            }
        } catch (Exception e) {
            setErrorMessage(request, "Error activating user: " + e.getMessage());
            redirect(request, response, "/users/view/" + userId);
        }
    }

    private void handleDeactivateUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        if (!isValidId(id)) {
            redirect(request, response, "/users");
            return;
        }

        Long userId = parseId(id);
        if (userId == null) {
            redirect(request, response, "/users");
            return;
        }

        try {
            Optional<Boolean> deactivated = userService.deactivateUser(userId);

            if (deactivated.isPresent() && deactivated.get()) {
                setSuccessMessage(request, "User deactivated successfully");
                redirect(request, response, "/users/view/" + userId);
            } else {
                setErrorMessage(request, "Failed to deactivate user");
                redirect(request, response, "/users/view/" + userId);
            }
        } catch (Exception e) {
            setErrorMessage(request, "Error deactivating user: " + e.getMessage());
            redirect(request, response, "/users/view/" + userId);
        }
    }
}
