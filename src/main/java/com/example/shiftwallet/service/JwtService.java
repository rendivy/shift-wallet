package com.example.shiftwallet.service;



import com.example.shiftwallet.dao.repository.RedisRepository;
import com.example.shiftwallet.dao.repository.UserRepository;
import com.example.shiftwallet.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtService {

    private final UserRepository userRepository;
    private final RedisRepository redisRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (SignatureException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    public Boolean validateToken(String token) {
        try {
            var userId = extractAllClaims(token).get("userId", String.class);
            var isUserExists = userRepository.findById(UUID.fromString(userId));
            var isTokenExists = redisRepository.tokenExists("Bearer " + token);
            return isUserExists.isPresent() && !isTokenExists;
        }
        catch (Exception e) {
            return false;
        }
    }

    public String extractTokenId(String token) {
        var claims = extractAllClaims(token.substring(7)).getId();
        
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    private Date getExpirationDateFromToken(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UUID userId, String email) {
        Map<String, Object> claims = new HashMap<>();

        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + expiration);
        UUID tokenId = UUID.randomUUID();


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .claim("userId", userId.toString())
                .setIssuedAt(issueDate)
                .setExpiration(expiredDate)
                .setId(tokenId.toString())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public User getUserFromToken(String token) {
        var userId = extractAllClaims(token).get("userId", String.class);
        return userRepository.findById(UUID.fromString(userId)).orElseThrow();
    }


}

