package com.nphuonha.servletkit.services.implementations.user;

import com.nphuonha.servletkit.models.User;

import com.nphuonha.servletkit.repository.interfaces.user.IUserRepository;
import com.nphuonha.servletkit.services.implementations.BaseService;
import com.nphuonha.servletkit.services.interfaces.user.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserService extends BaseService<User, Long> implements IUserService {

    protected UserService() {
        super();
    }

    @Inject
    public UserService(IUserRepository userRepository) {
        super();
        this.repository = userRepository;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return ((IUserRepository) this.repository).findByEmail(email);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return ((IUserRepository) this.repository).findByUsername(username);
    }

    @Override
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
    
    @Override
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
    
    @Override
    public Optional<User> createUser(User user) {
        try {
            validateUserForCreation(user);
            return save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Boolean> updateUser(User user) {
        try {
            validateUserForUpdate(user);
            return update(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Boolean> deleteUser(Long userId) {
        try {
            validateUserForDeletion(userId);
            return deleteById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
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
        
        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Business validation - check for duplicates
        if (isEmailAlreadyExists(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        if (isUsernameAlreadyExists(user.getUsername(), user.getId())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Password validation - if password is set, validate it
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            if (user.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }
            if (user.getPassword().length() > 100) {
                throw new IllegalArgumentException("Password cannot exceed 100 characters");
            }
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
        
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        
        if (user.getUsername().length() > 50) {
            throw new IllegalArgumentException("Username cannot exceed 50 characters");
        }
        
        if (!user.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        if (user.getPassword().length() > 100) {
            throw new IllegalArgumentException("Password cannot exceed 100 characters");
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
     * Simple email validation
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
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