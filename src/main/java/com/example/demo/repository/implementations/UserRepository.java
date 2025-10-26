package com.example.demo.repository.implementations;

import com.example.demo.models.User;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository extends BaseRepository<User, Long> {
    
    // No-arg constructor for CDI proxy support
    public UserRepository() {
        super();
    }
}
