package org.hyunggi.mygardenbe.auth.jwt.filter;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.auth.controller.AuthenticationController;
import org.hyunggi.mygardenbe.auth.jwt.domain.TokenType;
import org.hyunggi.mygardenbe.auth.jwt.service.JwtService;
import org.hyunggi.mygardenbe.auth.jwt.util.JwtAuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * JWT 인증 필터
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * JWT 서비스
     */
    private final JwtService jwtService;
    /**
     * Spring Security에서 Login시에 사용하는 UserDetailsService Interface
     */
    private final UserDetailsService userDetailsService;

    /**
     * Actuator 계정용 Email (Promehteus, Grafana 등의 모니터링 툴에서 사용)
     */
    @Value("${actuator.email:}")
    private String actuatorEmail;

    /**
     * Actuator 인증 Token (Promehteus, Grafana 등의 모니터링 툴에서 사용)
     */
    private UsernamePasswordAuthenticationToken actuatorAuthenticationToken;

    /**
     * Actuator 인증 Token을 생성 및 초기화 한다.
     * <br><br>
     * - Actuator Email이 설정되어 있고 유효한 경우, Actuator 인증 Token이 정상 발급<br>
     * - Actuator Email이 설정되어 있지 않거나 유효하지 않은 경우, Actuator 인증 Token이 null 이거나, 권한이 제대로 부여되지 않음
     */
    @PostConstruct
    protected void init() {
        if (isBlankActuatorEmail()) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(actuatorEmail);

        final Collection<? extends GrantedAuthority> authorities = validateEnabled(userDetails);

        actuatorAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
    }

    /**
     * Actuator Email이 빈 문자열인지 확인한다.
     *
     * @return Actuator Email이 빈 문자열인 경우 true, 아닌 경우 false
     */
    private boolean isBlankActuatorEmail() {
        return actuatorEmail.isBlank();
    }

    /**
     * UserDetails가 활성화 상태인지 확인한다.
     *
     * @param userDetails
     * @return 활성화 상태인 경우 가져온 authorities, 아닌 경우 empty authorities 반환한다.
     */
    private Collection<? extends GrantedAuthority> validateEnabled(final UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (!userDetails.isEnabled()) {
            log.error("Actuator user is not enabled");
            authorities.clear();
        }

        return authorities;
    }

    /**
     * JWT 인증 필터<br>
     * <br>
     * - JWT 토큰이 유효한 경우, SecurityContextHolder에 인증 정보를 설정한다.<br>
     * - JWT 토큰이 유효하지 않은 경우, SecurityContextHolder에 인증 정보를 설정하지 않는다.<br>
     *
     * @param request     Http 요청
     * @param response    Http 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      IO 예외
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessTokenText = JwtAuthUtil.extractTokenTextFromRequestHeader(request, TokenType.BEARER);
        log.info("request [{}] url: {}, accessToken: {}", request.getMethod(), request.getRequestURI(), accessTokenText);

        if (accessTokenText == null || isAuthApiPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationIfTokenIsValid(request, accessTokenText);

        filterChain.doFilter(request, response);
    }

    /**
     * 요청이 인증 API 경로인지 확인한다.
     *
     * @param request Http 요청
     * @return 인증 API 경로인 경우 true, 아닌 경우 false
     */
    private boolean isAuthApiPath(final HttpServletRequest request) {
        return request.getServletPath().contains(AuthenticationController.AUTH_BASE_API_PATH);
    }

    /**
     * 토큰이 유효한 경우, SecurityContextHolder에 인증 정보를 설정한다.
     *
     * @param request         Http 요청
     * @param accessTokenText Access Token 값
     */
    private void setAuthenticationIfTokenIsValid(final HttpServletRequest request, final String accessTokenText) {
        final Claims claims = jwtService.extractAllClaims(accessTokenText);
        final String userEmail = claims.getSubject();

        if (userEmail != null && isUserNotAuthenticated()) {
            UsernamePasswordAuthenticationToken authToken = buildAuthToken(userEmail);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    /**
     * 사용자가 인증되지 않은 경우 true를 반환한다.
     *
     * @return 사용자가 인증되지 않은 경우 true, 아닌 경우 false
     */
    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    /**
     * 사용자의 인증 정보가 담긴 UsernamePasswordAuthenticationToken을 생성한다.
     *
     * @param userEmail 사용자 Email
     * @return 사용자의 인증 정보가 담긴 UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken buildAuthToken(final String userEmail) {
        if (userEmail.equals(actuatorEmail)) {
            return actuatorAuthenticationToken;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
