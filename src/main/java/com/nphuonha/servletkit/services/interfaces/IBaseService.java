package com.nphuonha.servletkit.services.interfaces;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T, ID> {
    /**
     * Find entity by ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     */
    Optional<List<T>> findAll();
    
    /**
     * Save entity (insert or update)
     */
    Optional<T> save(T entity);
    
    /**
     * Update existing entity
     */
    Optional<Boolean> update(T entity);
    
    /**
     * Delete entity by ID
     */
    Optional<Boolean> deleteById(ID id);
    
    /**
     * Check if entity exists by ID
     */
    boolean existsById(ID id);
    
    /**
     * Count total entities
     */
    long count();
}
