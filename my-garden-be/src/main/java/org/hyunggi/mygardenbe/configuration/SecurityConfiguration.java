package org.hyunggi.mygardenbe.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.auth.controller.AuthenticationController;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtExceptionHandlerFilter;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.common.view.filter.HistoryModeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            AuthenticationController.AUTH_BASE_API_PATH + "/**",
            "/docs/index.html",
            "/assets/**",
            "/favicon.ico",
            "/",
            "/index.html",
            "/api/boards/notice/list",
            "/api/boards/notice/categories",
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
                                .requestMatchers(getOnlyAdminAccessNoticeApi()).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(new HistoryModeFilter(), AuthorizationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(AuthenticationController.AUTH_BASE_API_PATH + "/logout")
                                .addLogoutHandler(myLogoutHandler)
                                .logoutSuccessHandler((req, res, auth) -> {
                                            initializeResponse(res);
                                            SecurityContextHolder.clearContext();
                                        }
                                )
                );

        return http.build();
    }

    private RequestMatcher[] getOnlyAdminAccessNoticeApi() {
        return new RequestMatcher[]{
                antMatcher(HttpMethod.POST, "/api/boards/notice"),
                antMatcher(HttpMethod.PUT, "/api/boards/notice/**"),
                antMatcher(HttpMethod.DELETE, "/api/boards/notice/**")
        };
    }

    private void initializeResponse(final HttpServletResponse res) {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse<Object> apiResponse = ApiResponse.of(HttpStatus.OK, null);

        try {
            res.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (IOException e) {
            final String logoutSuccessHandlerError = "LogoutSuccessHandler error";

            log.warn(logoutSuccessHandlerError, e);
            throw new IllegalArgumentException(logoutSuccessHandlerError);
        }
    }
}
