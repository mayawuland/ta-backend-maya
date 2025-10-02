package com.indomarco.indostore.controller;

import com.indomarco.indostore.entity.User;
import com.indomarco.indostore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * Controller for user authentication endpoints, including registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    /**
     * Constructor for AuthController.
     *
     * @param userService The UserService used to handle authentication logic.
     */
    public AuthController(UserService userService) { this.userService = userService; }

    /**
     * Register a new user.
     *
     * @param user The user object containing email, name, and password.
     * @return ResponseEntity containing the created user data (without password) or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User saved = userService.register(user);
            saved.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "User registered successfully",
                    "data", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "User registration failed",
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * Login a user and generate an authentication token.
     *
     * @param body Map containing "email" and "password" keys.
     * @return ResponseEntity containing a JWT/token if successful or error message if failed.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            String token = userService.login(email, password);

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Login failed",
                    "error", e.getMessage()
            ));
        }
    }

}
