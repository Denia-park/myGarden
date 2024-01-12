package org.hyunggi.mygardenbe.configuration;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.auth.controller.AuthController;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            AuthController.AUTH_API_PATH + "/**",
            "/docs/index.html",
            "/actuator/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider myAuthenticationProvider;
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
                .authenticationProvider(myAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(AuthController.AUTH_API_PATH + "/logout")
                                .addLogoutHandler(myLogoutHandler)
                                .logoutSuccessHandler((req, res, auth) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
