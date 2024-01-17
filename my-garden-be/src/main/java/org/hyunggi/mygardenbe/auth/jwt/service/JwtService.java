package org.hyunggi.mygardenbe.auth.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.common.exception.InvalidTokenRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public Claims extractAllClaims(final String token) {
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
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException e) {
            final String errorMessage = String.format("JWT getClaimsBody Exception [%s] -> token : %s, msg : %s",
                    e.getClass().getName(), token, e.getMessage());
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
        addRolesToClaims(extraClaims, userDetails);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private void addRolesToClaims(final Map<String, Object> extraClaims, final UserDetails userDetails) {
        String authorities = convertString(userDetails.getAuthorities());
        extraClaims.put("roles", authorities);
    }

    private String convertString(final Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public Collection<? extends GrantedAuthority> convertStringToAuthorities(final String rolesText) {
        final String[] roles = rolesText.split(",");

        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
