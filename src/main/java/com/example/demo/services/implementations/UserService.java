package com.example.demo.services.implementations;

import com.example.demo.models.User;
import com.example.demo.repository.implementations.UserRepository;
import com.example.demo.services.interfaces.IBaseService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService extends BaseService<User, Long> implements IBaseService<User, Long> {
    
    @Inject
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
    
    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            
            Optional<List<User>> allUsers = findAll();
            return allUsers.flatMap(users -> 
                users.stream()
                    .filter(user -> email.equals(user.getEmail()))
                    .findFirst()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by email: " + email, e);
        }
    }
    
    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            
            Optional<List<User>> allUsers = findAll();
            return allUsers.flatMap(users -> 
                users.stream()
                    .filter(user -> username.equals(user.getUsername()))
                    .findFirst()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by username: " + username, e);
        }
    }
    
    /**
     * Find active users
     */
    public Optional<List<User>> findActiveUsers() {
        try {
            Optional<List<User>> allUsers = findAll();
            return allUsers.map(users -> 
                users.stream()
                    .filter(user -> User.Status.ACTIVE.getValue().equals(user.getStatus()))
                    .toList()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error finding active users", e);
        }
    }
    
    /**
     * Activate user
     */
    public Optional<Boolean> activateUser(Long userId) {
        try {
            Optional<User> userOpt = findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setStatus(User.Status.ACTIVE.getValue());
                return update(user);
            }
            return Optional.of(false);
        } catch (Exception e) {
            throw new RuntimeException("Error activating user: " + userId, e);
        }
    }
    
    /**
     * Deactivate user
     */
    public Optional<Boolean> deactivateUser(Long userId) {
        try {
            Optional<User> userOpt = findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setStatus(User.Status.INACTIVE.getValue());
                return update(user);
            }
            return Optional.of(false);
        } catch (Exception e) {
            throw new RuntimeException("Error deactivating user: " + userId, e);
        }
    }
    
    /**
     * Create user with validation
     */
    public Optional<User> createUser(User user) {
        try {
            validateUserForCreation(user);
            return save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update user with validation
     */
    public Optional<Boolean> updateUser(User user) {
        try {
            validateUserForUpdate(user);
            return update(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete user with validation
     */
    public Optional<Boolean> deleteUser(Long userId) {
        try {
            validateUserForDeletion(userId);
            return deleteById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate user for creation
     */
    private void validateUserForCreation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Business validation
        if (isEmailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        if (isUsernameAlreadyExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Set default status if not provided
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("ACTIVE");
        }
    }
    
    /**
     * Validate user for update
     */
    private void validateUserForUpdate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update");
        }
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Business validation
        if (isEmailAlreadyExists(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        if (isUsernameAlreadyExists(user.getUsername(), user.getId())) {
            throw new IllegalArgumentException("Username already exists");
        }
    }
    
    /**
     * Validate user for deletion
     */
    private void validateUserForDeletion(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        if (!existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
    }
    
    /**
     * Check if email already exists (for creation)
     */
    private boolean isEmailAlreadyExists(String email) {
        Optional<User> existingUser = findByEmail(email);
        return existingUser.isPresent();
    }
    
    /**
     * Check if email already exists (for update - exclude current user)
     */
    private boolean isEmailAlreadyExists(String email, Long currentUserId) {
        Optional<User> existingUser = findByEmail(email);
        return existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId);
    }
    
    /**
     * Check if username already exists (for creation)
     */
    private boolean isUsernameAlreadyExists(String username) {
        Optional<User> existingUser = findByUsername(username);
        return existingUser.isPresent();
    }
    
    /**
     * Check if username already exists (for update - exclude current user)
     */
    private boolean isUsernameAlreadyExists(String username, Long currentUserId) {
        Optional<User> existingUser = findByUsername(username);
        return existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId);
    }
}