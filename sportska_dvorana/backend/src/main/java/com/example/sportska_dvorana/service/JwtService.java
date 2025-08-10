package com.example.sportska_dvorana.service;
import io.jsonwebtoken.*;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.example.sportska_dvorana.config.JwtProperties;
import com.example.sportska_dvorana.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = new SecretKeySpec(
            jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS512.getJcaName()
        );
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, User user) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            String email = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (email.equals(user.getEmail()) && expiration.after(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}