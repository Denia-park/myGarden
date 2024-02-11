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

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${actuator.email:}")
    private String actuatorEmail;
    private UsernamePasswordAuthenticationToken actuatorAuthenticationToken;

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

    private boolean isBlankActuatorEmail() {
        return actuatorEmail.isBlank();
    }

    private Collection<? extends GrantedAuthority> validateEnabled(final UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (!userDetails.isEnabled()) {
            log.error("Actuator user is not enabled");
            authorities.clear();
        }

        return authorities;
    }

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

    private boolean isAuthApiPath(final HttpServletRequest request) {
        return request.getServletPath().contains(AuthenticationController.AUTH_BASE_API_PATH);
    }

    private void setAuthenticationIfTokenIsValid(final HttpServletRequest request, final String accessTokenText) {
        final Claims claims = jwtService.extractAllClaims(accessTokenText);
        final String userEmail = claims.getSubject();

        if (userEmail != null && isUserNotAuthenticated()) {
            UsernamePasswordAuthenticationToken authToken = buildAuthToken(userEmail);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

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
