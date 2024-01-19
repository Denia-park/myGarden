package org.hyunggi.mygardenbe.auth.jwt.filter;

import jakarta.servlet.ServletException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.jwt.service.JwtService;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JwtAuthenticationFilterTest extends IntegrationTestSupport {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Token에서 정보를 빼내어 Authentication 객체를 만들어 SecurityContext에 저장한다")
    void doFilterInternal() throws ServletException, IOException {
        //given
        final String email = "test@test.com";
        final String password = "test1234!";

        final Long memberId = authenticationService.signUp(email, password);
        final AuthenticationResponse authenticationResponse = authenticationService.login(email, password);
        final String accessToken = authenticationResponse.accessToken();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + accessToken);

        //when
        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());

        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();

        //then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo(email);
        assertThat(authentication.getAuthorities())
                .hasSize(1)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");

        context.setAuthentication(null);
    }
}
