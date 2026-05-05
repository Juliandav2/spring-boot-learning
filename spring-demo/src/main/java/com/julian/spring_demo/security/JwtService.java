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
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    private SecretKey getKey () {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken (String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public String extractEmail (String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid (String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
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
