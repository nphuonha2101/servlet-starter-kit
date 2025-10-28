package com.nphuonha.servletkit.http.controllers.base;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Base controller that provides common functionality for all controllers
 * including URL segment parsing and routing
 */
public abstract class BaseController extends HttpServlet {
    
    /**
     * Parse URL path segments
     * Example: /users/view/123 -> ["view", "123"]
     * Example: /users/create -> ["create"]
     * Example: /users -> []
     */
    protected String[] parsePathSegments(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            return new String[0];
        }
        
        // Remove leading slash and split by slash
        String path = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        return path.split("/");
    }
    
    /**
     * Get action from URL segments
     * @param segments URL segments array
     * @param defaultAction default action if no segments
     * @return action string
     */
    protected String getAction(String[] segments, String defaultAction) {
        return segments.length > 0 ? segments[0] : defaultAction;
    }
    
    /**
     * Get ID from URL segments
     * @param segments URL segments array
     * @return ID string or null if not present
     */
    protected String getId(String[] segments) {
        return segments.length > 1 ? segments[1] : null;
    }
    
    /**
     * Get parameter from URL segments by index
     * @param segments URL segments array
     * @param index parameter index
     * @return parameter string or null if not present
     */
    protected String getParameter(String[] segments, int index) {
        return segments.length > index ? segments[index] : null;
    }
    
    /**
     * Get multiple parameters from URL segments
     * @param segments URL segments array
     * @param startIndex starting index (inclusive)
     * @param endIndex ending index (exclusive)
     * @return array of parameters
     */
    protected String[] getParameters(String[] segments, int startIndex, int endIndex) {
        if (startIndex >= segments.length || startIndex < 0) {
            return new String[0];
        }
        
        int actualEndIndex = Math.min(endIndex, segments.length);
        int length = actualEndIndex - startIndex;
        
        if (length <= 0) {
            return new String[0];
        }
        
        String[] result = new String[length];
        System.arraycopy(segments, startIndex, result, 0, length);
        return result;
    }
    
    /**
     * Get all parameters from URL segments starting from index
     * @param segments URL segments array
     * @param startIndex starting index (inclusive)
     * @return array of parameters
     */
    protected String[] getParametersFrom(String[] segments, int startIndex) {
        return getParameters(segments, startIndex, segments.length);
    }
    
    /**
     * Get parameters as a map with named keys
     * @param segments URL segments array
     * @param keys parameter keys in order
     * @return map of parameter name to value
     */
    protected Map<String, String> getParametersAsMap(String[] segments, String... keys) {
        Map<String, String> params = new HashMap<>();
        
        for (int i = 0; i < keys.length && i < segments.length; i++) {
            params.put(keys[i], segments[i]);
        }
        
        return params;
    }
    
    /**
     * Parse segments into structured data
     * Example: /users/123/posts/456/comments -> {action: "users", id: "123", subAction: "posts", subId: "456", subSubAction: "comments"}
     * @param segments URL segments array
     * @return map of structured data
     */
    protected Map<String, Object> parseSegments(String[] segments) {
        Map<String, Object> result = new HashMap<>();
        
        if (segments.length > 0) {
            result.put("action", segments[0]);
        }
        if (segments.length > 1) {
            result.put("id", segments[1]);
        }
        if (segments.length > 2) {
            result.put("subAction", segments[2]);
        }
        if (segments.length > 3) {
            result.put("subId", segments[3]);
        }
        if (segments.length > 4) {
            result.put("subSubAction", segments[4]);
        }
        if (segments.length > 5) {
            result.put("subSubId", segments[5]);
        }
        
        // Add remaining segments as array
        if (segments.length > 6) {
            String[] remaining = new String[segments.length - 6];
            System.arraycopy(segments, 6, remaining, 0, remaining.length);
            result.put("remaining", remaining);
        }
        
        return result;
    }
    
    /**
     * Check if segments match a pattern
     * @param segments URL segments array
     * @param pattern pattern to match (e.g., ["users", "view", "*"])
     * @return true if matches
     */
    protected boolean matchesPattern(String[] segments, String... pattern) {
        if (segments.length != pattern.length) {
            return false;
        }
        
        for (int i = 0; i < segments.length; i++) {
            if (!"*".equals(pattern[i]) && !segments[i].equals(pattern[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get segment count
     * @param segments URL segments array
     * @return number of segments
     */
    protected int getSegmentCount(String[] segments) {
        return segments.length;
    }
    
    /**
     * Check if has specific number of segments
     * @param segments URL segments array
     * @param count expected count
     * @return true if matches
     */
    protected boolean hasSegments(String[] segments, int count) {
        return segments.length == count;
    }
    
    /**
     * Check if has minimum number of segments
     * @param segments URL segments array
     * @param minCount minimum count
     * @return true if has at least minCount segments
     */
    protected boolean hasMinimumSegments(String[] segments, int minCount) {
        return segments.length >= minCount;
    }
    
    /**
     * Check if ID is valid (not null and not empty)
     * @param id ID string to validate
     * @return true if valid
     */
    protected boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty();
    }
    
    /**
     * Parse ID to Long with error handling
     * @param id ID string to parse
     * @return Optional Long or null if invalid
     */
    protected Long parseId(String id) {
        if (!isValidId(id)) {
            return null;
        }
        
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Redirect to a URL
     * @param request HTTP request
     * @param response HTTP response
     * @param url URL to redirect to
     */
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            response.sendRedirect(request.getContextPath() + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Redirect to a URL with error handling
     * @param request HTTP request
     * @param response HTTP response
     * @param url URL to redirect to
     * @param fallbackUrl fallback URL if redirect fails
     */
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String url, String fallbackUrl) {
        try {
            response.sendRedirect(request.getContextPath() + url);
        } catch (IOException e) {
            try {
                response.sendRedirect(request.getContextPath() + fallbackUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Get request parameter with default value
     * @param request HTTP request
     * @param name parameter name
     * @param defaultValue default value if parameter not found
     * @return parameter value or default
     */
    protected String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get request parameter as Integer with default value
     * @param request HTTP request
     * @param name parameter name
     * @param defaultValue default value if parameter not found or invalid
     * @return parameter value or default
     */
    protected Integer getParameterAsInt(HttpServletRequest request, String name, Integer defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get request parameter as Long with default value
     * @param request HTTP request
     * @param name parameter name
     * @param defaultValue default value if parameter not found or invalid
     * @return parameter value or default
     */
    protected Long getParameterAsLong(HttpServletRequest request, String name, Long defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get request parameter as Boolean with default value
     * @param request HTTP request
     * @param name parameter name
     * @param defaultValue default value if parameter not found
     * @return parameter value or default
     */
    protected Boolean getParameterAsBoolean(HttpServletRequest request, String name, Boolean defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Create a map of parameters for easy access
     * @param request HTTP request
     * @return map of all parameters
     */
    protected Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                params.put(key, values[0]);
            }
        });
        return params;
    }
    
    /**
     * Check if request is AJAX request
     * @param request HTTP request
     * @return true if AJAX request
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
    
    /**
     * Get client IP address
     * @param request HTTP request
     * @return client IP address
     */
    protected String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
