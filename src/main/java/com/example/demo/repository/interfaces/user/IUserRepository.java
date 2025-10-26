package com.example.demo.repository.interfaces.user;

import com.example.demo.models.User;
import com.example.demo.repository.interfaces.IBaseRepository;

import java.util.Optional;

public interface IUserRepository extends IBaseRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
