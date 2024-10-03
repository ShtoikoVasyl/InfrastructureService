package edu.shtoiko.infrastructureservice.utils;

import edu.shtoiko.infrastructureservice.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    @Value("${jwt.rest.key}")
    private String secretKey;

    @Value("${jwt.rest.expiration_time.access}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.rest.expiration_time.refresh}")
    private Long refreshTokenExpirationTime;

    private final String tokenType = "Bearer";

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationTime);
    }

    private String generateToken(UserDetails userDetails, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        return generateToken(claims, userDetails, expirationTime);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private final Function<Claims, List<Role>> getRoles = claims -> {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
            .map(Role::new)
            .collect(Collectors.toList());
    };

    public List<Role> extractRoles(String token) {
        return extractClaims(token, getRoles);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isRefreshTokenNotExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis() + accessTokenExpirationTime));
    }

    public boolean isRefreshTokenFresh(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis() - accessTokenExpirationTime));
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

}
