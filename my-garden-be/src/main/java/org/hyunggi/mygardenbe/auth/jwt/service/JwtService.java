package org.hyunggi.mygardenbe.auth.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.common.exception.InvalidTokenRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        final JwtParser jwtParser = getJwtParser();

        return getClaimsBody(jwtParser, token);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaimsBody(final JwtParser jwtParser, final String token) {
        final Claims body;
        try {
            body = jwtParser
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            final String errorMessage = String.format("Request to parse expired JWT -> token : %s, msg : %s", token, e.getMessage());
            log.warn(errorMessage);
            throw new InvalidTokenRequestException(errorMessage);
        } catch (UnsupportedJwtException e) {
            final String errorMessage = String.format("Request to parse unsupported JWT -> token : %s, msg : %s", token, e.getMessage());
            log.warn(errorMessage);
            throw new InvalidTokenRequestException(errorMessage);
        } catch (MalformedJwtException e) {
            final String errorMessage = String.format("Request to parse invalid JWT -> token : %s, msg : %s", token, e.getMessage());
            log.warn(errorMessage);
            throw new InvalidTokenRequestException(errorMessage);
        } catch (SignatureException e) {
            final String errorMessage = String.format("Request to parse JWT with invalid signature -> token : %s, msg : %s", token, e.getMessage());
            log.warn(errorMessage);
            throw new InvalidTokenRequestException(errorMessage);
        } catch (IllegalArgumentException e) {
            final String errorMessage = String.format("Request to parse illegal argument -> inputText : %s, msg : %s", token, e.getMessage());
            log.warn(errorMessage);
            throw new InvalidTokenRequestException(errorMessage);
        }

        return body;
    }

    public String generateAccessToken(final UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateRefreshToken(final UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(final Map<String, Object> extraClaims, final UserDetails userDetails, final long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())) && isNotTokenExpired(token);
    }

    private boolean isNotTokenExpired(final String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
