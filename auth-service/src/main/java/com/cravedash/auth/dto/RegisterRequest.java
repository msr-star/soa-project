package com.cravedash.auth.dto;

import com.cravedash.auth.entity.Role;

public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private Role role;
    private String partnerSecret;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public RegisterRequest(String email, String password, String fullName, Role role, String partnerSecret) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.partnerSecret = partnerSecret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPartnerSecret() {
        return partnerSecret;
    }

    public void setPartnerSecret(String partnerSecret) {
        this.partnerSecret = partnerSecret;
    }
}
