package com.example.batiss.music.GestionDesSons.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUnit {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private long expirationTime;

    public String generatedToken(String username){
        Map<String, Object> cleams = new HashMap<>();
        return createToken(cleams,username);
    }

    private String createToken(Map<String, Object> cleams, String object) {

        return Jwts.builder()
                .setClaims(cleams)
                .setSubject(object)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte [] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username= extractedUsername(token);
        return (username.equals(userDetails.getUsername()) && !IsTokenExpirated(token));
    }

    private boolean IsTokenExpirated(String token) {
        return extractCleam(token)
                .getExpiration()
                .before(new Date());
    }

    public String extractedUsername(String token) {
        return extractCleam(token).getSubject();
    }

    private Claims extractCleam(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

