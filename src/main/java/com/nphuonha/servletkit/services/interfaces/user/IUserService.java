package com.nphuonha.servletkit.services.interfaces.user;

import com.nphuonha.servletkit.models.User;
import com.nphuonha.servletkit.services.interfaces.IBaseService;

import java.util.Optional;

public interface IUserService extends IBaseService<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<Boolean> activateUser(Long id);
    Optional<Boolean> deactivateUser(Long id);
    Optional<User> createUser(User user);
    Optional<Boolean> updateUser(User user);
    Optional<Boolean> deleteUser(Long id);
}
