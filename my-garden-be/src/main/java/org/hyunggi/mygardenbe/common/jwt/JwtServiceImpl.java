package org.hyunggi.mygardenbe.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
