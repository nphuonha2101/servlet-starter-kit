package com.nphuonha.servletkit.repository.implementations.user;

import com.nphuonha.servletkit.models.User;

import com.nphuonha.servletkit.repository.implementations.BaseRepository;
import com.nphuonha.servletkit.repository.interfaces.user.IUserRepository;
import com.nphuonha.servletkit.repository.mappers.BaseModelMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.jdbi.v3.core.Handle;

import java.util.Optional;

@ApplicationScoped
public class UserRepository extends BaseRepository<User, Long> implements IUserRepository {

    public UserRepository() {
        super();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Handle handle = getHandle()) {
            String sql = "SELECT * FROM " + getTableName() + " WHERE email = :email";
            User result = handle.createQuery(sql)
                    .bind("email", email)
                    .map(new BaseModelMapper<>(entityClass))
                    .findOne()
                    .orElse(null);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity by email: " + email, e);
        } finally {
            closeHandle();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Handle handle = getHandle()) {
            String sql = "SELECT * FROM " + getTableName() + " WHERE username = :username";
            User result = handle.createQuery(sql)
                    .bind("username", username)
                    .map(new BaseModelMapper<>(entityClass))
                    .findOne()
                    .orElse(null);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            throw new RuntimeException("Error finding entity by username: " + username, e);
        } finally {
            closeHandle();
        }
    }
}
