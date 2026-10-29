package com.cravedash.auth.controller;

import com.cravedash.auth.dto.AuthResponse;
import com.cravedash.auth.dto.LoginRequest;
import com.cravedash.auth.dto.RegisterRequest;
import com.cravedash.auth.dto.UserResponse;
import com.cravedash.auth.security.JwtUtils;
import com.cravedash.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", "Missing or invalid token"));
        }
        String token = authHeader.substring(7);
        boolean isValid = jwtUtils.validateToken(token);
        if (isValid) {
            Claims claims = jwtUtils.parseClaims(token);
            Map<String, Object> resp = new HashMap<>();
            resp.put("valid", true);
            resp.put("email", claims.getSubject());
            resp.put("role", claims.get("role"));
            resp.put("userId", claims.get("userId"));
            resp.put("fullName", claims.get("fullName"));
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", "Expired or tampered token"));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "X-User-Role", required = false) String userRole,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAdmin(userRole, authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER or ADMIN role required to list users"));
        }
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("id") Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorizedAdmin(userRole, authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER or ADMIN role required to delete users"));
        }
        try {
            authService.deleteUser(id);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "User with ID " + id + " successfully deleted from PostgreSQL mydb");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    private boolean isAuthorizedAdmin(String userRole, String authHeader) {
        if ("RESTAURANT_OWNER".equalsIgnoreCase(userRole) || "ADMIN".equalsIgnoreCase(role(userRole))) {
            return true;
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                Claims claims = jwtUtils.parseClaims(token);
                String role = (String) claims.get("role");
                return "RESTAURANT_OWNER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
            }
        }
        return false;
    }

    private String role(String r) {
        return r != null ? r : "";
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("service", "auth-service", "status", "UP", "port", 8081));
    }
}
