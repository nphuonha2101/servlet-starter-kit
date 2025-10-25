package com.example.demo.services.implementations;

import com.example.demo.models.BaseModel;
import com.example.demo.repository.interfaces.IBaseRepository;
import com.example.demo.services.interfaces.IBaseService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseModel, ID> implements IBaseService<T, ID> {
    
    protected final IBaseRepository<T, ID> repository;
    protected final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public BaseService(IBaseRepository<T, ID> repository) {
        this.repository = repository;
        
        Type genericSuperClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
        this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
    
    @Override
    public Optional<T> findById(ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity by id: " + id, e);
        }
    }
    
    @Override
    public Optional<List<T>> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all entities", e);
        }
    }
    
    @Override
    public Optional<T> save(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }
    
    @Override
    public Optional<Boolean> update(T entity) {
        try {
            return repository.update(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity", e);
        }
    }
    
    @Override
    public Optional<Boolean> deleteById(ID id) {
        try {
            return repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity by id: " + id, e);
        }
    }
    
    @Override
    public boolean existsById(ID id) {
        try {
            return findById(id).isPresent();
        } catch (Exception e) {
            throw new RuntimeException("Error checking entity existence by id: " + id, e);
        }
    }
    
    @Override
    public long count() {
        try {
            Optional<List<T>> allEntities = findAll();
            return allEntities.map(List::size).orElse(0);
        } catch (Exception e) {
            throw new RuntimeException("Error counting entities", e);
        }
    }
    
    
    /**
     * Get repository instance for advanced operations
     */
    protected IBaseRepository<T, ID> getRepository() {
        return repository;
    }
}
