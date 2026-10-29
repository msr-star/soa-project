package com.cravedash.restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String cuisine;

    @Column(nullable = false, length = 250)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @Column(name = "owner_email", length = 100)
    private String ownerEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Restaurant() {
    }

    public Restaurant(String name, String cuisine, String address, String phone, Double rating, String imageUrl, Boolean isOpen, String ownerEmail) {
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
        this.phone = phone;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.isOpen = isOpen;
        this.ownerEmail = ownerEmail;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
