package com.example.WorkforceManagement.security;

public class AuthenticatedUser {
    private final Long id;
    private final String email;
    private final String role;

    public AuthenticatedUser(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return "AuthenticatedUser{id=" + id + ", email=" + email + ", role=" + role + "}";
    }
}