package com.cravedash.restaurant.service;

import com.cravedash.restaurant.dto.MenuItemRequest;
import com.cravedash.restaurant.dto.RestaurantRequest;
import com.cravedash.restaurant.entity.MenuItem;
import com.cravedash.restaurant.entity.Restaurant;
import com.cravedash.restaurant.repository.MenuItemRepository;
import com.cravedash.restaurant.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @PostConstruct
    public void seedInitialData() {
        if (!restaurantRepository.existsById(1L)) {
            Restaurant r1 = new Restaurant(
                    "The Royal Biryani & Kebab Palace",
                    "Hyderabadi, Mughlai, Indian",
                    "Plot 42, Jubilee Hills, Sector 5",
                    "+91 98765 43210",
                    4.8,
                    "https://images.pexels.com/photos/1624487/pexels-photo-1624487.jpeg?auto=compress&cs=tinysrgb&w=800",
                    true,
                    "owner@cravedash.com"
            );
            restaurantRepository.save(r1);
        }

        if (!restaurantRepository.existsById(2L)) {
            Restaurant r2 = new Restaurant(
                    "Artisanal Pasta & Woodfired Crust",
                    "Italian, Neapolitan, Gourmet",
                    "12 Gourmet Boulevard, High Street",
                    "+91 91234 56789",
                    4.7,
                    "https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&w=800",
                    true,
                    "owner@cravedash.com"
            );
            restaurantRepository.save(r2);
        }

        if (!restaurantRepository.existsById(3L)) {
            Restaurant r3 = new Restaurant(
                    "Green Harvest Bowls & Grills",
                    "Healthy, Mediterranean, Fresh",
                    "8 Eco Plaza, Tech Boulevard",
                    "+91 94567 12345",
                    4.9,
                    "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=800",
                    true,
                    "owner@cravedash.com"
            );
            restaurantRepository.save(r3);
        }
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + id));
    }

    // ==========================================
    // INSERTION: Add new restaurant into PostgreSQL mydb
    // ==========================================
    @Transactional
    public Restaurant createRestaurant(RestaurantRequest req) {
        Restaurant restaurant = new Restaurant(
                req.getName(),
                req.getCuisine(),
                req.getAddress(),
                req.getPhone(),
                req.getRating() != null ? req.getRating() : 4.5,
                req.getImageUrl() != null && !req.getImageUrl().isEmpty() ? req.getImageUrl() : "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800",
                req.getIsOpen() != null ? req.getIsOpen() : true,
                req.getOwnerEmail() != null ? req.getOwnerEmail() : "owner@cravedash.com"
        );
        return restaurantRepository.save(restaurant);
    }

    // ==========================================
    // DELETION: Remove restaurant and its menu items from PostgreSQL mydb
    // ==========================================
    @Transactional
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant not found with ID: " + id);
        }
        menuItemRepository.deleteByRestaurantId(id);
        restaurantRepository.deleteById(id);
    }

    public List<MenuItem> getMenuItems(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with ID: " + id));
    }

    // ==========================================
    // INSERTION: Add new menu dish into PostgreSQL mydb
    // ==========================================
    @Transactional
    public MenuItem addMenuItem(Long restaurantId, MenuItemRequest req) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant not found with ID: " + restaurantId);
        }

        MenuItem item = new MenuItem(
                restaurantId,
                req.getName(),
                req.getDescription(),
                req.getPrice(),
                req.getCategory() != null ? req.getCategory() : "Main Course",
                req.getIsVeg() != null ? req.getIsVeg() : false,
                req.getIsAvailable() != null ? req.getIsAvailable() : true,
                req.getImageUrl() != null && !req.getImageUrl().isEmpty() ? req.getImageUrl() : "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600"
        );

        return menuItemRepository.save(item);
    }

    // ==========================================
    // DELETION: Remove a dish from PostgreSQL mydb
    // ==========================================
    @Transactional
    public void deleteMenuItem(Long itemId) {
        if (!menuItemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Menu item not found with ID: " + itemId);
        }
        menuItemRepository.deleteById(itemId);
    }

    @Transactional
    public MenuItem toggleAvailability(Long itemId) {
        MenuItem item = getMenuItemById(itemId);
        item.setIsAvailable(!item.getIsAvailable());
        return menuItemRepository.save(item);
    }
}
