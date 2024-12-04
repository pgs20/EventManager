package dev.petrov.security.jwt;

import dev.petrov.dto.usersDto.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenManager.class);
    private final SecretKey key;
    private final Long expirationTime;

    public JwtTokenManager(@Value("${jwt.secret-key}") String key, @Value("${jwt.lifetime}") Long expirationTime) {
        this.key = new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.expirationTime = expirationTime;
    }

    public String generateToken(String login, String role) {
        log.info("Генерация ключа для {}", login);
        return Jwts
                .builder()
                .subject(login)
                .claim("role", role)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        log.info("Извлечение логина из токена");
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwt) {
        log.info("Извлечение роли из токена");
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .get("role", String.class);
    }
}
