package com.vodica.order_system.security;

import com.vodica.order_system.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.accessSecret}")
    private String accessSecret;

    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.exp}")
    private Long exp;

    @Value("${jwt.refreshExp}")
    private Long refreshExp;

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .signWith(getKey())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .compact();
    }

    public String generateToken(String username, Map<String,Object> claims){
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .signWith(getKey())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .compact();
    }

    public String extractUsernameFromToken(String token){
            return parseToken(token).getPayload().getSubject();
    }

    public String extractUsernameFromRefreshToken(String token){
        return parseRefreshToken(token).getPayload().getSubject();
    }

    public String generateRefreshToken(User user){
        return Jwts.builder()
                .signWith(getRefreshKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExp))
                .claim("name",user.getName())
                .claim("role",user.getRole())
                .subject(user.getEmail())
                .compact();
    }

    public boolean verifyToken(String token){
        try{
           parseToken(token);
            return true;
        } catch (Exception e) {
            log.error("JWT parse token error: ",e);
            return false;
        }

    }

    public boolean verifyRefreshToken(String refreshToken){
        try{
            parseRefreshToken(refreshToken);
            return true;
        } catch (Exception e) {
            log.error("JWT parse token error: ",e);
            return false;
        }

    }

    private Jws<Claims> parseToken(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
    }

    private Jws<Claims> parseRefreshToken(String token){
        return Jwts.parser().verifyWith(getRefreshKey()).build().parseSignedClaims(token);
    }



    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshKey(){
        return Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }
}
