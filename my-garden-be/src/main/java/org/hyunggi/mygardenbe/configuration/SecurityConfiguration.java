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
import org.hyunggi.mygardenbe.member.domain.Role;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Security Configuration
 * <br><br>
 * - Security 설정
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfiguration {
    /**
     * 해당 하는 URL은 JWT 인증을 거치지 않음
     */
    private static final String[] WHITE_LIST_URL = {
            AuthenticationController.AUTH_BASE_API_PATH + "/**",
            "/docs/index.html",
            "/assets/**",
            "/favicon.ico",
            "/",
            "/index.html",
            "/api/boards/categories",
            "/api/daily-routine/study-hours/without-login",
    };

    /**
     * JWT 인증 필터
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * 로그아웃 핸들러
     */
    private final LogoutHandler myLogoutHandler;

    /**
     * Actuator URL
     */
    @Value("${actuator.url:/default}")
    private String actuatorUrl;

    /**
     * Security Filter Chain
     * <br><br>
     * - Security Filter Chain 설정
     * - csrf, cors 비활성화
     * - 요청에 따른 권한 설정
     * - session 비활성화
     * - JWT 인증 필터, JWT 예외 처리 필터 추가
     * - HistoryModeFilter 추가
     * - 로그아웃 핸들러 추가
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(getActuatorAllUrl()).hasRole(Role.ACTUATOR.toString())
                                .requestMatchers(getOnlyAdminAccessNoticeApi()).hasRole(Role.ADMIN.toString())
                                .requestMatchers(getReadBoardsApi()).permitAll()
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
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

    /**
     * Actuator URL
     *
     * @return Actuator URL (모든 URL)
     */
    private String getActuatorAllUrl() {
        return actuatorUrl + "/**";
    }

    /**
     * Admin만 접근이 가능한 공지사항 API
     *
     * @return 공지사항 API
     */
    private RequestMatcher[] getOnlyAdminAccessNoticeApi() {
        return new RequestMatcher[]{
                antMatcher(HttpMethod.POST, "/api/boards/notice"),
                antMatcher(HttpMethod.PUT, "/api/boards/notice/**"),
                antMatcher(HttpMethod.DELETE, "/api/boards/notice/**")
        };
    }

    /**
     * 공지사항, TIL 게시판 조회 API (댓글 포함)
     *
     * @return 공지사항, TIL 게시판 조회 API
     */
    private RequestMatcher[] getReadBoardsApi() {
        return new RequestMatcher[]{
                antMatcher(HttpMethod.GET, "/api/boards/notice/**"),
                antMatcher(HttpMethod.GET, "/api/boards/learn/**"),
                antMatcher(HttpMethod.GET, "/api/boards/comments/**"),
        };
    }

    /**
     * Logout 성공시 처리될 응답 초기화
     *
     * @param res Http 응답
     */
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
