package com.gateway.util;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
  
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String SECRET_KEY = "mySecretKeyWhichShouldBeVeryLongForSecurity12345";

    public Claims extractClaims(String token) {
        return Jwts.parser().
        		verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
  
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
