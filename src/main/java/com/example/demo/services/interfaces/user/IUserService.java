package com.example.demo.services.interfaces.user;

import com.example.demo.models.User;
import com.example.demo.services.interfaces.IBaseService;

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
