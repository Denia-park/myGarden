package org.hyunggi.mygardenbe.common.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationInMills;

    @Override
    public String getToken(final String key, final Object value) {
        return Jwts.builder()
                .setHeader(buildHeader())
                .setClaims(Map.of(key, value))
                .setExpiration(buildExpirationTime())
                .signWith(buildKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims getClaims(final String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(buildKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT token: {}", token);

            throw new ExpiredJwtException(null, null, "Expired JWT token");
        } catch (JwtException e) {
            log.warn("유효하지 않은 JWT token: {}", token);

            throw new JwtException("Invalid JWT token");
        }
    }

    private Map<String, Object> buildHeader() {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        return headerMap;
    }

    private Date buildExpirationTime() {
        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + expirationInMills);

        return expTime;
    }

    private Key buildKey() {
        return new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }
}
