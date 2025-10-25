package com.example.demo.repository.implementations;

import com.example.demo.models.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository extends BaseRepository<User, Long> {
}
