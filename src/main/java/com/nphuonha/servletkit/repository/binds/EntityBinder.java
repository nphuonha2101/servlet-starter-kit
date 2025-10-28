package com.nphuonha.servletkit.repository.binds;

import com.nphuonha.servletkit.core.utils.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class EntityBinder {
    
    /**
     * Generate INSERT SQL for an entity
     */
    public static String generateInsertSQL(String tableName, Field[] fields) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder values = new StringBuilder(" VALUES (");
        
        boolean first = true;
        for (Field field : fields) {
            if (field.getName().equals("id")) continue;
            
            if (!first) {
                sql.append(", ");
                values.append(", ");
            }
            
            sql.append(StringUtils.camelToSnakeCase(field.getName()));
            values.append(":").append(field.getName());
            first = false;
        }
        
        sql.append(")").append(values).append(")");
        return sql.toString();
    }
    
    /**
     * Generate UPDATE SQL for an entity
     */
    public static String generateUpdateSQL(String tableName, Field[] fields) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        
        boolean first = true;
        for (Field field : fields) {
            if (field.getName().equals("id") || field.getName().equals("createdAt")) continue;
            
            if (!first) {
                sql.append(", ");
            }
            
            sql.append(StringUtils.camelToSnakeCase(field.getName())).append(" = :").append(field.getName());
            first = false;
        }
        
        sql.append(" WHERE id = :id");
        return sql.toString();
    }
    
    /**
     * Set timestamps for entity
     */
    public static void setTimestamps(Object entity, boolean isUpdate) {
        try {
            Field updatedAtField = getField(entity.getClass(), "updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(entity, LocalDateTime.now());
            
            if (!isUpdate) {
                Field createdAtField = getField(entity.getClass(), "createdAt");
                createdAtField.setAccessible(true);
                createdAtField.set(entity, LocalDateTime.now());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting timestamps", e);
        }
    }
    
    /**
     * Helper method to get a field from the class or its parent classes
     */
    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
    
}
