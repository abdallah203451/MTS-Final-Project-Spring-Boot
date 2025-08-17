package com.example.WorkforceManagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {
    private SecurityUtils() {}

    /**
     * Returns the authenticated user's id (Long).
     * Throws RuntimeException if unauthenticated or cannot resolve id.
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return ((AuthenticatedUser) principal).getId();
        }

        // If for some reason principal is a String (username) or UserDetails,
        // attempt to extract numeric id from string; otherwise fail loudly.
        if (principal instanceof String) {
            String s = (String) principal;
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Principal is username string, cannot resolve user id. Consider using AuthenticatedUser principal.");
            }
        }

        if (principal instanceof UserDetails) {
            // In this code path, UserDetails doesn't expose id. Prefer AuthenticatedUser.
            throw new RuntimeException("UserDetails principal in security context; implement mapping to user id.");
        }

        throw new RuntimeException("Unable to resolve current user id from principal");
    }

    /**
     * Optional: returns the principal as AuthenticatedUser if available, else null.
     */
    public static AuthenticatedUser getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object p = auth.getPrincipal();
        if (p instanceof AuthenticatedUser) return (AuthenticatedUser) p;
        return null;
    }
}