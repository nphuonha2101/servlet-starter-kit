package com.nphuonha.servletkit.repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T, ID> {
    Optional<T> findById(ID id);
    Optional<List<T>> findAll();
    Optional<T> save(T entity);
    Optional<Boolean> update(T entity);
    Optional<Boolean> deleteById(ID id);
}