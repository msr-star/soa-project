package com.cravedash.restaurant.controller;

import com.cravedash.restaurant.dto.MenuItemRequest;
import com.cravedash.restaurant.dto.RestaurantRequest;
import com.cravedash.restaurant.entity.MenuItem;
import com.cravedash.restaurant.entity.Restaurant;
import com.cravedash.restaurant.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    private boolean isAuthorizedOwner(HttpServletRequest request) {
        String role = (String) request.getAttribute("userRole");
        if (role == null || role.isBlank()) {
            role = request.getHeader("X-User-Role");
        }
        return "RESTAURANT_OWNER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role) || "INTERNAL_SERVICE".equalsIgnoreCase(role);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // ==========================================
    // INSERTION: Register New Restaurant (RBAC: Owner Only)
    // ==========================================
    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantRequest request, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to register restaurants"));
        }
        try {
            Restaurant created = restaurantService.createRestaurant(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==========================================
    // DELETION: Remove Restaurant & Menu (RBAC: Owner Only)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to delete restaurants"));
        }
        try {
            restaurantService.deleteRestaurant(id);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Restaurant ID " + id + " and its menu items deleted from PostgreSQL mydb");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable("id") Long id) {
        return ResponseEntity.ok(restaurantService.getMenuItems(id));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<?> getMenuItemById(@PathVariable("itemId") Long itemId) {
        try {
            return ResponseEntity.ok(restaurantService.getMenuItemById(itemId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // ==========================================
    // INSERTION: Add Dish to Menu (RBAC: Owner Only)
    // ==========================================
    @PostMapping("/{id}/items")
    public ResponseEntity<?> addMenuItem(@PathVariable("id") Long id, @RequestBody MenuItemRequest request, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to add menu items"));
        }
        try {
            MenuItem item = restaurantService.addMenuItem(id, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ==========================================
    // DELETION: Delete Dish from Menu (RBAC: Owner Only)
    // ==========================================
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable("itemId") Long itemId, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to delete menu items"));
        }
        try {
            restaurantService.deleteMenuItem(itemId);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Menu item ID " + itemId + " successfully deleted from PostgreSQL mydb");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/items/{itemId}/availability")
    public ResponseEntity<?> toggleAvailability(@PathVariable("itemId") Long itemId, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to toggle stock availability"));
        }
        try {
            MenuItem item = restaurantService.toggleAvailability(itemId);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("service", "restaurant-service", "status", "UP", "port", 8082));
    }
}
