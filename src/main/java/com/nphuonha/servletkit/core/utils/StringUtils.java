package com.nphuonha.servletkit.core.utils;

/**
 * Utility class for string operations
 */
public class StringUtils {
    
    /**
     * Convert camelCase to snake_case
     * Example: "createdAt" -> "created_at"
     * 
     * @param camelCase the camelCase string to convert
     * @return the snake_case string
     */
    public static String camelToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}

