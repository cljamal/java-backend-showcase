package com.sultanov.present_project.features.auth.services;

import com.sultanov.present_project.features.users.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String cleanToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        return token.substring(7);
    }

    public String generateToken(User user, String jti) {
        return Jwts.builder()
                .id(jti)
                .subject(user.getPhone())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractJti(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }


    public String extractPhone(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSignKey() {
         return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }
}