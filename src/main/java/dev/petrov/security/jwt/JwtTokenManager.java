package dev.petrov.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenManager.class);
    private final SecretKey key;
    private final Long expirationTime;

    public JwtTokenManager(@Value("${jwt.lifetime}") Long expirationTime) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.expirationTime = expirationTime;
    }

    public String generateToken(String login) {
        return Jwts
                .builder()
                .setSubject(login)
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
