package com.auth.util;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//import java.util.Date;
//import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecret";

    // **Generate JWT Token**
    public String generateToken(String username, String role) {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)  // Store role in token
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    // **Extract Username from JWT**
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // **Extract specific claim**
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // **Validate JWT Token**
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
