package com.auth.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private String SECRET_KEY = "mySecretKeyWhichShouldBeVeryLongForSecurity12345";

//    public JwtUtil() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            SECRET_KEY = Base64.getEncoder().encodeToString(sk.getEncoded());
//            logger.info("JWT Secret key generated successfully.");
//        } catch (NoSuchAlgorithmException e) {
//            logger.error("Failed to generate secret key: {}", e.getMessage(), e);
//        }
//    }

    // **Generate JWT Token**
    public String generateToken(String username, List<String>  role) {
        String token = Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey())
            .compact();

        logger.debug("JWT token generated for user: {}", username);
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

        logger.debug("Generated internal JWT with INTERNAL_AUTH role for user: {}", username);
        return token;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
        		.verifyWith(getSigningKey())
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

    
//    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
}
