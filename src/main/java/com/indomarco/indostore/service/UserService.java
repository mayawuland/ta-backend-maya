package com.indomarco.indostore.service;

import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing User entities.
 * 
 * Provides functionality for registering users, logging in, and finding users by token.
 */
@Service
public class UserService {
    private final UserRepository repo;

    /**
     * Constructor for UserService.
     *
     * @param repo The UserRepository used for database operations.
     */
    public UserService(UserRepository repo) { this.repo = repo; }

    /**
     * Registers a new user.
     *
     * @param user The user to register.
     * @return The saved User entity.
     */
    public User register(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        return repo.save(user);
    }

    /**
     * Logs in a user and generates a token.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A newly generated token for the user session.
     */
    public String login(String email, String password) {
        Optional<User> found = repo.findByEmail(email);
        if (found.isEmpty() || !found.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        User user = found.get();
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        repo.save(user);
        return token;
    }

     /**
     * Finds a user by their authentication token.
     *
     * @param token The token to search for.
     * @return The User entity associated with the token.
     */
    public User findByToken(String token) {
        return repo.findByToken(token)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
    }
}
