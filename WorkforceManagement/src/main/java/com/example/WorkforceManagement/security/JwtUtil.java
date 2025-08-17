package com.example.WorkforceManagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessExpirationMillis;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-expiration-minutes}") long accessMinutes) {
        // secret should be base64 or hex or raw; here we support a simple raw string for Keys.hmacShaKeyFor()
        byte[] bytes = secret.getBytes();
        this.key = Keys.hmacShaKeyFor(bytes);
        this.accessExpirationMillis = accessMinutes * 60 * 1000;
    }

    public String generateAccessToken(Long userId, String role, String email) {
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + accessExpirationMillis);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validate(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Long getUserIdFromToken(String token) {
        Claims c = validate(token).getBody();
        return Long.parseLong(c.getSubject());
    }

    public String getRoleFromToken(String token) {
        return (String) validate(token).getBody().get("role");
    }
}