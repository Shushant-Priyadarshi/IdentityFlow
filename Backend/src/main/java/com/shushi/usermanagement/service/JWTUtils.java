package com.shushi.usermanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final SecretKey key;
    private static final long EXPIRATION_TIME = 86400000;

    public JWTUtils() {
        String secretString = "62c544bbe33df9c320608639674603caf104038549c56dbc500e3a3ec3b99e8d";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        key = new SecretKeySpec(keyBytes,"HmacSHA256");
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String,Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    private  <T> T extractClaims(String token, Function<Claims,T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }


    public boolean isTokenValid(String token,UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }
}
