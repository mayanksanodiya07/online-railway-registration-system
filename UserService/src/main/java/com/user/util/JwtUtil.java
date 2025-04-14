package com.user.util;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mySecretKeyWhichShouldBeVeryLongForSecurity12345"; // Use env var or config

    public String generateToken(String username, List<String>  role) {
        String token = Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey())
            .compact();

        return token;
    }
    public String generateInternalToken(String username, List<String> roles) {
        
        List<String> updatedRoles = new ArrayList<>(roles);
        updatedRoles.add("INTERNAL_AUTH");

        String token = Jwts.builder()
                .subject(username)
                .claim("roles", updatedRoles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();

        return token;
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List<?>) {
            List<?> rawList = (List<?>) rolesObj;
            return rawList.stream()
                          .filter(item -> item instanceof String)
                          .map(item -> (String) item)
                          .toList();
        }

        return List.of(); // return empty list if no roles or wrong format
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
