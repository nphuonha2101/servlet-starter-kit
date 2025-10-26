package com.example.demo.http.controllers.web;

import com.example.demo.http.controllers.base.BaseController;
import com.example.demo.http.pages.Page;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Base web controller that provides common functionality for web controllers
 * including page rendering and error handling
 */
public abstract class BaseWebController extends BaseController {
    
    /**
     * Render a page with common attributes
     * @param request HTTP request
     * @param response HTTP response
     * @param page Page object to render
     */
    protected void renderPage(HttpServletRequest request, HttpServletResponse response, Page page) {
        try {
            page.render(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error rendering page: " + e.getMessage());
        }
    }
    
    /**
     * Render a page with custom attributes
     * @param request HTTP request
     * @param response HTTP response
     * @param page Page object to render
     * @param attributes Additional attributes to set
     */
    protected void renderPage(HttpServletRequest request, HttpServletResponse response, Page page, 
                             java.util.Map<String, Object> attributes) {
        try {
            // Set additional attributes
            if (attributes != null) {
                attributes.forEach(request::setAttribute);
            }
            
            page.render(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error rendering page: " + e.getMessage());
        }
    }
    
    /**
     * Handle errors by redirecting to error page or showing error message
     * @param request HTTP request
     * @param response HTTP response
     * @param errorMessage Error message to display
     */
    protected void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) {
        try {
            // Set error attributes
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorCode", "500");
            request.setAttribute("pageTitle", "Error");
            
            // Create error page
            Page errorPage = new Page()
                .setTitle("Error")
                .setPageName("error")
                .setLayout("app");
            
            errorPage.render(request, response);
        } catch (Exception e) {
            try {
                // Fallback: redirect to home page
                response.sendRedirect(request.getContextPath() + "/");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Handle validation errors
     * @param request HTTP request
     * @param response HTTP response
     * @param validationErrors Map of field errors
     * @param redirectUrl URL to redirect back to
     */
    protected void handleValidationError(HttpServletRequest request, HttpServletResponse response, 
                                        java.util.Map<String, String> validationErrors, String redirectUrl) {
        try {
            // Set validation errors
            request.setAttribute("validationErrors", validationErrors);
            
            // Redirect back to form
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } catch (IOException e) {
            handleError(request, response, "Validation error occurred");
        }
    }
    
    /**
     * Check if user is authenticated (override in concrete controllers)
     * @param request HTTP request
     * @return true if authenticated
     */
    protected boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return session.getAttribute("currentUser") != null;
        }
        return true;
    }
    
    /**
     * Require authentication for protected routes
     * @param request HTTP request
     * @param response HTTP response
     * @return true if authenticated, false if redirected to login
     */
    protected boolean requireAuth(HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            try {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            } catch (IOException e) {
                handleError(request, response, "Authentication required");
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get current user from session (override in concrete controllers)
     * @param request HTTP request
     * @return current user object or null
     */
    protected Object getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return session.getAttribute("currentUser");
        }
        return null;
    }
    
    /**
     * Set flash message in session
     * @param request HTTP request
     * @param type Message type (success, error, warning, info)
     * @param message Message content
     */
    protected void setFlashMessage(HttpServletRequest request, String type, String message) {
        request.getSession().setAttribute("flashType", type);
        request.getSession().setAttribute("flashMessage", message);
    }
    
    /**
     * Get and clear flash message from session
     * @param request HTTP request
     * @return Map with flash message data or null
     */
    protected java.util.Map<String, String> getFlashMessage(HttpServletRequest request) {
        String type = (String) request.getSession().getAttribute("flashType");
        String message = (String) request.getSession().getAttribute("flashMessage");
        
        if (type != null && message != null) {
            // Clear flash message
            request.getSession().removeAttribute("flashType");
            request.getSession().removeAttribute("flashMessage");
            
            java.util.Map<String, String> flash = new java.util.HashMap<>();
            flash.put("type", type);
            flash.put("message", message);
            return flash;
        }
        
        return null;
    }
    
    /**
     * Create a success flash message
     * @param request HTTP request
     * @param message Success message
     */
    protected void setSuccessMessage(HttpServletRequest request, String message) {
        setFlashMessage(request, "success", message);
    }
    
    /**
     * Create an error flash message
     * @param request HTTP request
     * @param message Error message
     */
    protected void setErrorMessage(HttpServletRequest request, String message) {
        setFlashMessage(request, "error", message);
    }
    
    /**
     * Create a warning flash message
     * @param request HTTP request
     * @param message Warning message
     */
    protected void setWarningMessage(HttpServletRequest request, String message) {
        setFlashMessage(request, "warning", message);
    }
    
    /**
     * Create an info flash message
     * @param request HTTP request
     * @param message Info message
     */
    protected void setInfoMessage(HttpServletRequest request, String message) {
        setFlashMessage(request, "info", message);
    }
}
