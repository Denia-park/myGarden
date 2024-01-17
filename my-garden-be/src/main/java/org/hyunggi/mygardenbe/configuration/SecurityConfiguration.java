package org.hyunggi.mygardenbe.configuration;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.controller.AuthenticationController;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtExceptionHandlerFilter;
import org.hyunggi.mygardenbe.common.view.filter.HistoryModeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            AuthenticationController.AUTH_BASE_API_PATH + "/**",
            "/docs/index.html",
            "/assets/**",
            "/favicon.ico",
            "/",
            "/index.html",
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler myLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(new HistoryModeFilter(), AuthorizationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(AuthenticationController.AUTH_BASE_API_PATH + "/logout")
                                .addLogoutHandler(myLogoutHandler)
                                .logoutSuccessHandler((req, res, auth) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
