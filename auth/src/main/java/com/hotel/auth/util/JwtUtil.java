package com.hotel.auth.util;

import com.hotel.auth.model.User;
import com.hotel.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;

    // Encode the secret key for better security
    private String secret = "pG5+XlP2Fs1dzA5mV3QhKa8Lw7TqYtJcRfNpMvBdU6ZoXgK9HrWyEjC2Vu0IbOuyryuryu4387ergfhueruyr##$$DFDFERR^$dvhjefjhfhe";
    private long expiration = 86400000; // 1 day in milliseconds

    public String generateToken(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Handle case where user is not found
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Invalid username and password");
        }

        User user = userOptional.get();
        String role = user.getRole();

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Store the role in the token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()) // Secure signing
                .compact();
    }
}