package com.admin.util;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
 
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String SECRET_KEY = "mySecretKeyWhichShouldBeVeryLongForSecurity12345";
	
	public String generateInternalToken() {
		String token = Jwts.builder()
				.subject("admin-service")
				.issuer("admin-service")  
	    		.claim("serviceRole", "ADMIN-SERVICE")  
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

        return List.of();  
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
