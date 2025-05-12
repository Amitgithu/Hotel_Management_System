package com.example.apigatewayservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("pG5+XlP2Fs1dzA5mV3QhKa8Lw7TqY35785899tJcRfNpMvBdU6ZoXgK9HrWyEjC2Vu0IbOuyryuryu4387ergfhueruyr##$$DFDFERR^$dvhjefjhfhe".getBytes());

    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token.replace("Bearer ", ""));
            return true; // Token is valid
        } catch (Exception e) {
            System.err.println("JWT Validation Error: " + e.getMessage());
            return false; // Invalid token
        }
    }


    // Extract Role from JWT Token
    public String extractRole(String token) {
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parse(token.replace("Bearer ", ""))
                .getPayload(); // Correct way to get claims

        System.out.println("Claims: " + claims);

        return claims.get("role", String.class);
    }

}
