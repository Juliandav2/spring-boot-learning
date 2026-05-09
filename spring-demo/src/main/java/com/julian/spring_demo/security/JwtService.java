package com.julian.spring_demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mi-clave-secreta-muy-larga-para-jwt-debe-tener-256-bits";
    private static final long ACCESS_TOKEN_EXPIRATION = 100 * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    private SecretKey getKey () {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken (String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshTokenValue () {
        return java.util.UUID.randomUUID().toString();
    }

    public String extractEmail (String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid (String token, String email) {
        try {
            return extractEmail(token).equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired (String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims (String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
