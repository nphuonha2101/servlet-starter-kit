package com.nphuonha.servletkit.repository.interfaces.user;

import com.nphuonha.servletkit.models.User;
import com.nphuonha.servletkit.repository.interfaces.IBaseRepository;

import java.util.Optional;

public interface IUserRepository extends IBaseRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
