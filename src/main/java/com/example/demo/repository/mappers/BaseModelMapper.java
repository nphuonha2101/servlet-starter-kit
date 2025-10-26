package com.example.demo.repository.mappers;

import com.example.demo.core.utils.StringUtils;
import com.example.demo.models.BaseModel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BaseModelMapper<T extends BaseModel> implements RowMapper<T> {
    private final Class<T> entityClass;
    
    public BaseModelMapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public T map(ResultSet rs, StatementContext ctx) throws SQLException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            
            // Get all fields from class hierarchy (including parent classes)
            Field[] fields = getFieldsFromHierarchy(entityClass);
            
            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = StringUtils.camelToSnakeCase(field.getName());
                
                Object value = rs.getObject(columnName);
                if (value != null) {
                    if (field.getType() == LocalDateTime.class && value instanceof java.sql.Timestamp) {
                        field.set(entity, ((java.sql.Timestamp) value).toLocalDateTime());
                    } else {
                        field.set(entity, value);
                    }
                }
            }
            
            return entity;
        } catch (Exception e) {
            throw new SQLException("Error mapping result set to entity: " + entityClass.getSimpleName(), e);
        }
    }
    
    private Field[] getFieldsFromHierarchy(Class<?> clazz) {
        java.util.List<Field> fields = new java.util.ArrayList<>();
        Class<?> currentClass = clazz;
        
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                fields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        
        return fields.toArray(new Field[0]);
    }
    

}
