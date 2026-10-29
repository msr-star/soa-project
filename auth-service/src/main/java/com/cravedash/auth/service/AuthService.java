package com.cravedash.auth.service;

import com.cravedash.auth.dto.AuthResponse;
import com.cravedash.auth.dto.LoginRequest;
import com.cravedash.auth.dto.RegisterRequest;
import com.cravedash.auth.dto.UserResponse;
import com.cravedash.auth.entity.Role;
import com.cravedash.auth.entity.User;
import com.cravedash.auth.repository.UserRepository;
import com.cravedash.auth.security.JwtUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostConstruct
    public void seedInitialUsers() {
        if (!userRepository.existsByEmail("customer@cravedash.com")) {
            User customer = new User(
                    "customer@cravedash.com",
                    passwordEncoder.encode("customer123"),
                    "Rahul Sharma (Customer)",
                    Role.CUSTOMER
            );
            userRepository.save(customer);
        }

        if (!userRepository.existsByEmail("owner@cravedash.com")) {
            User owner = new User(
                    "owner@cravedash.com",
                    passwordEncoder.encode("owner123"),
                    "Chef Sanjeev (Partner)",
                    Role.RESTAURANT_OWNER
            );
            userRepository.save(owner);
        }

        if (!userRepository.existsByEmail("admin@cravedash.com")) {
            User admin = new User(
                    "admin@cravedash.com",
                    passwordEncoder.encode("admin123"),
                    "Admin Operations Lead (Admin)",
                    Role.ADMIN
            );
            userRepository.save(admin);
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }

        Role role = req.getRole() != null ? req.getRole() : Role.CUSTOMER;
        if (role == Role.RESTAURANT_OWNER) {
            String partnerSecret = req.getPartnerSecret();
            if (partnerSecret == null || !"CRAVEDASH_PARTNER_2026".equals(partnerSecret.trim())) {
                throw new IllegalArgumentException("Registration as Restaurant Owner requires a valid Partner Invitation Code!");
            }
        }
        User user = new User(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getFullName(),
                role
        );

        // Insertion into PostgreSQL mydb
        User saved = userRepository.save(user);

        String token = jwtUtils.generateToken(saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole());
        return new AuthResponse(token, saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(), "User registered successfully");
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + req.getEmail()));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password credentials");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole(), "Login successful");
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getRole(), u.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User does not exist with ID: " + id);
        }
        // Deletion from PostgreSQL mydb
        userRepository.deleteById(id);
    }
}
