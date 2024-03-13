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

/**
 * JWT Service
 */
@Slf4j
@Service
public class JwtService {
    /**
     * JWT용 Secret Key
     */
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * JWT 만료 시간
     */
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * JWT Refresh Token 만료 시간
     */
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * JWT 토큰에서 Payload의 Claim 목록 추출
     *
     * @param token 토큰
     * @return Payload의 Claim 목록
     */
    public Claims extractAllClaims(final String token) {
        final JwtParser jwtParser = getJwtParser();

        return getClaimsBody(jwtParser, token);
    }

    /**
     * JWT Parse를 위한 JwtParser 생성
     *
     * @return JwtParser
     */
    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
    }

    /**
     * JWT Secret Key 생성
     *
     * @return Secret Key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰에서 jwtParser를 이용하여 Payload의 Claim 목록 추출 <br>
     * <br>
     * - 토큰이 만료되었거나, 유효하지 않은 토큰일 경우, log를 남기고 InvalidTokenRequestException 예외 발생
     *
     * @param jwtParser JwtParser
     * @param token     토큰
     * @return Payload의 Claim 목록
     */
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

    /**
     * userDetail로부터 AccessToken 생성
     *
     * @param userDetails Spring Security의 UserDetails Interface (유저 정보)
     * @return AccessToken
     */
    public String generateAccessToken(final UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    /**
     * userDetail로부터 RefreshToken 생성
     *
     * @param userDetails Spring Security의 UserDetails Interface (유저 정보)
     * @return RefreshToken
     */
    public String generateRefreshToken(final UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * userDetail와 추가 Claim을 이용하여 만료 시간을 설정한 JWT 토큰 생성
     *
     * @param extraClaims 추가 Claim
     * @param userDetails Spring Security의 UserDetails Interface (유저 정보)
     * @param expiration  만료 시간
     * @return JWT 토큰
     */
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

    /**
     * userDetail의 권한을 JWT에도 담기 위해서, 추가 Claim에 roles를 추가
     *
     * @param extraClaims 추가 Claim
     * @param userDetails Spring Security의 UserDetails Interface (유저 정보)
     */
    private void addRolesToClaims(final Map<String, Object> extraClaims, final UserDetails userDetails) {
        String authorities = convertString(userDetails.getAuthorities());
        extraClaims.put("roles", authorities);
    }

    /**
     * 권한을 String으로 변환
     *
     * @param authorities 권한 목록
     * @return 권한 String
     */
    private String convertString(final Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * JWT 토큰의 roles Claim을 이용하여 권한 목록 생성
     *
     * @param rolesText roles Claim 값
     * @return 권한 목록 (GrantedAuthority의 Collection)
     */
    public Collection<? extends GrantedAuthority> convertStringToAuthorities(final String rolesText) {
        final String[] roles = rolesText.split(",");

        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
