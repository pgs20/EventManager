package dev.petrov.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenManager {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenManager.class);
    private final SecretKey key;
    private final Long expirationTime;

    public JwtTokenManager(@Value("${jwt.lifetime}") Long expirationTime) {
        this.key = new SecretKeySpec(generationSecretKey().getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.expirationTime = expirationTime;
    }

    public String generateToken(String login) {
        log.info("Генерация токена для {}", login);
        return Jwts
                .builder()
                .subject(login)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        log.info("Получение логина из токена {}", jwt);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    private String generationSecretKey() {
        byte[] key = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        return Base64.getEncoder().encodeToString(key);
    }
}
