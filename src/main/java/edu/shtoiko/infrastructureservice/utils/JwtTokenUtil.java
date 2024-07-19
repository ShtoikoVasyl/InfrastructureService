package edu.shtoiko.infrastructureservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${jwttokenutil.secretkey}")
    private String SECRET_KEY = null;
    @Value("${jwttokenutil.expirationtime}")
    private long EXPIRATION_TIME; // 2 хвилини в мілісекундах

    public String createToken(long terminalId) {
        return Jwts.builder()
                .setSubject(String.valueOf(terminalId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            Date expiration = claims.getBody().getExpiration();
            return expiration.after(new Date());
        } catch (SignatureException | ExpiredJwtException e) {
            return false;
        }
    }

    public long getUsernameFromToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token);
        return Long.parseLong(claims.getBody().getSubject());
    }

}
