package org.hyunggi.mygardenbe.mock.security;

import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.hyunggi.mygardenbe.configuration.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class MyCustomTestSecurityConfiguration {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, LogoutHandler mylogoutHandler) throws Exception {
        final JwtAuthenticationFilter jwtAuthFilterForTest = new JwtAuthenticationFilter(null);

        return new SecurityConfiguration(
                jwtAuthFilterForTest,
                mylogoutHandler)
                .securityFilterChain(http);
    }
}
