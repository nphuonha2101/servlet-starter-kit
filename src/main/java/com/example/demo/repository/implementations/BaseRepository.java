package com.example.demo.repository.implementations;

import com.example.demo.core.database.JdbiDatabaseConnection;
import com.example.demo.models.BaseModel;

import com.example.demo.repository.interfaces.IBaseRepository;
import com.example.demo.repository.mappers.BaseModelMapper;
import com.example.demo.repository.binds.EntityBinder;
import org.jdbi.v3.core.Handle;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends BaseModel, ID> implements IBaseRepository<T, ID> {
    
    protected Class<T> entityClass;
    protected String tableName;
    
    @SuppressWarnings("unchecked")
    public BaseRepository() {
        // Don't extract entityClass here to avoid ClassCastException in proxy
    }
    
    // Lazy initialization of entityClass
    @SuppressWarnings("unchecked")
    protected void ensureEntityClassInitialized() {
        if (this.entityClass == null) {
            Type genericSuperClass = getClass().getGenericSuperclass();
            if (genericSuperClass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
                Type[] actualArgs = parameterizedType.getActualTypeArguments();
                if (actualArgs.length > 0) {
                    this.entityClass = (Class<T>) actualArgs[0];
                }
            }
        }
    }
    
    /**
     * Get table name from entity class. You can override this method to return a custom table name.
     * Example: User -> users, Product -> products, ProductCategory -> product_categories
     * @return table name
     */
    protected String getTableName() {
        ensureEntityClassInitialized();
        if (tableName == null) {
            String className = entityClass.getSimpleName();
            String snakeCase = className.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            
            // Convert to plural
            if (snakeCase.endsWith("y")) {
                // category -> categories, company -> companies
                tableName = snakeCase.substring(0, snakeCase.length() - 1) + "ies";
            } else if (snakeCase.endsWith("s") || snakeCase.endsWith("sh") || snakeCase.endsWith("ch") || 
                       snakeCase.endsWith("x") || snakeCase.endsWith("z")) {
                // class -> classes, dish -> dishes, box -> boxes
                tableName = snakeCase + "es";
            } else {
                // user -> users, product -> products
                tableName = snakeCase + "s";
            }
        }
        return tableName;
    }
    
    /**
     * Get database handle from connection pool
     */
    protected Handle getHandle() {
        return JdbiDatabaseConnection.openHandle();
    }
    
    /**
     * Close database handle
     */
    protected void closeHandle() {
        JdbiDatabaseConnection.closeHandle();
    }
    
    @Override
    public Optional<T> findById(ID id) {
        try (Handle handle = getHandle()) {
            String sql = "SELECT * FROM " + getTableName() + " WHERE id = :id";
            T result = handle.createQuery(sql)
                    .bind("id", id)
                    .map(new BaseModelMapper<>(entityClass))
                    .findOne()
                    .orElse(null);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity by id: " + id, e);
        } finally {
            closeHandle();
        }
    }
    
    @Override
    public Optional<List<T>> findAll() {
        try (Handle handle = getHandle()) {
            String sql = "SELECT * FROM " + getTableName() + " ORDER BY created_at DESC";
            List<T> results = handle.createQuery(sql)
                    .map(new BaseModelMapper<>(entityClass))
                    .list();
            return Optional.of(results);
        } catch (Exception e) {
            throw new RuntimeException("Error finding all entities", e);
        } finally {
            closeHandle();
        }
    }
    
    @Override
    public Optional<T> save(T entity) {
        try (Handle handle = getHandle()) {
            if (entity.getId() == null) {
                return insert(entity, handle);
            } else {
                return update(entity, handle);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity", e);
        } finally {
            closeHandle();
        }
    }
    
    @Override
    public Optional<Boolean> update(T entity) {
        try (Handle handle = getHandle()) {
            update(entity, handle);
            return Optional.of(true);
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity", e);
        } finally {
            closeHandle();
        }
    }
    
    @Override
    public Optional<Boolean> deleteById(ID id) {
        try (Handle handle = getHandle()) {
            String sql = "DELETE FROM " + getTableName() + " WHERE id = :id";
            int rowsAffected = handle.createUpdate(sql)
                    .bind("id", id)
                    .execute();
            return Optional.of(rowsAffected > 0);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity by id: " + id, e);
        } finally {
            closeHandle();
        }
    }
    
    /**
     * Insert new entity into database
     */
    private Optional<T> insert(T entity, Handle handle) {
        ensureEntityClassInitialized();
        EntityBinder.setTimestamps(entity, false);
        
        Field[] fields = entityClass.getDeclaredFields();
        String sql = EntityBinder.generateInsertSQL(getTableName(), fields);
        
        Long generatedId = handle.createUpdate(sql)
                .bindBean(entity)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one();
        
        entity.setId(generatedId);
        return Optional.of(entity);
    }
    
    /**
     * Update existing entity in database
     */
    private Optional<T> update(T entity, Handle handle) {
        ensureEntityClassInitialized();
        EntityBinder.setTimestamps(entity, true);
        
        Field[] fields = entityClass.getDeclaredFields();
        String sql = EntityBinder.generateUpdateSQL(getTableName(), fields);
        
        int rowsAffected = handle.createUpdate(sql)
                .bindBean(entity)
                .execute();
        
        return rowsAffected > 0 ? Optional.of(entity) : Optional.empty();
    }
}