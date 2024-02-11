package org.hyunggi.mygardenbe.auth.jwt.filter;

import jakarta.servlet.ServletException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.auth.jwt.domain.Token;
import org.hyunggi.mygardenbe.auth.jwt.entity.TokenEntity;
import org.hyunggi.mygardenbe.auth.jwt.repository.TokenRepository;
import org.hyunggi.mygardenbe.auth.jwt.service.JwtService;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.auth.service.response.AuthenticationResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
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
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;

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


    @Test
    @DisplayName("actuatorEmail이 등록되고, 사용자가 활성화 상태라면, buildToken 메서드가 항상 같은 Authentication 객체를 반환한다")
    void buildTokenMethodAlwaysReturnsSameAuthenticationObjectIfActuatorEmailIsRegistered() throws ServletException, IOException {
        //given
        final String actuatorEmail = "test@test.com";
        final String actuatorPassword = "test1234!";
        signUpActuatorMember(actuatorEmail, actuatorPassword);

        final AuthenticationResponse authenticationResponse = authenticationService.login(actuatorEmail, actuatorPassword);
        final String accessToken = authenticationResponse.accessToken();

        ReflectionTestUtils.setField(jwtAuthenticationFilter, "actuatorEmail", actuatorEmail);
        jwtAuthenticationFilter.init();
        final Authentication actuatorAuthenticationToken = (Authentication) ReflectionTestUtils.getField(jwtAuthenticationFilter, "actuatorAuthenticationToken");

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + accessToken);

        //when
        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        jwtAuthenticationFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());

        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();

        //then
        assertThat(actuatorAuthenticationToken.getPrincipal()).isEqualTo(authentication.getPrincipal());
        assertThat(actuatorAuthenticationToken.getAuthorities()).isEqualTo(authentication.getAuthorities());
    }

    private void signUpActuatorMember(final String actuatorEmail, final String actuatorPassword) {
        final Member actuator = new Member(actuatorEmail, actuatorPassword, Role.ACTUATOR, true);
        final MemberEntity actuatorMember = memberRepository.save(MemberEntity.of(actuator, passwordEncoder));

        final Token token = Token.createBearerToken("testToken");
        tokenRepository.save(TokenEntity.of(token, actuatorMember.getId()));
    }

    @Test
    @DisplayName("인증이 필요 없는 API 경로 요청 시, 필터링을 건너뛴다")
    void filterBypassesUnauthenticatedPaths() throws ServletException, IOException {
        //given
        final String passApi = "/api/auth/login";
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath(passApi);

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        //when
        final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        assertThat(filterChain.getRequest()).isNotNull();
        assertThat(filterChain.getResponse()).isNotNull();
    }

    @Test
    @DisplayName("actuatorEmail이 등록되지 않으면, @PostConstruct로 실행되는 init 메서드가 actuatorAuthenticationToken을 설정하지 않는다")
    void initMethodDoesNotSetActuatorAuthenticationTokenIfActuatorEmailIsNotRegistered() {
        //given
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "actuatorEmail", "");

        //when
        jwtAuthenticationFilter.init();

        //then
        assertThat(ReflectionTestUtils.getField(jwtAuthenticationFilter, "actuatorAuthenticationToken")).isNull();
    }

    @Test
    @DisplayName("actuatorEmail이 등록되면, @PostConstruct로 실행되는 init 메서드가 actuatorAuthenticationToken을 설정한다")
    void initMethodSetsActuatorAuthenticationTokenIfActuatorEmailIsRegistered() {
        //given
        final String actuatorEmail = "test@test.com";
        signUpActuatorMember(actuatorEmail, "test1234!");

        ReflectionTestUtils.setField(jwtAuthenticationFilter, "actuatorEmail", actuatorEmail);

        //when
        jwtAuthenticationFilter.init();

        //then
        assertThat(ReflectionTestUtils.getField(jwtAuthenticationFilter, "actuatorAuthenticationToken")).isNotNull();
    }

    @Test
    @DisplayName("actuatorEmail이 등록되었지만, 사용자가 비활성화 상태라면, actuatorAuthenticationToken은 Authorities가 비어있다")
    void initMethodDoesNotSetActuatorAuthenticationTokenIfActuatorUserIsNotEnabled() {
        //given
        final String actuatorEmail = "test@test.com";
        final Member actuator = new Member(actuatorEmail, "test1234!", Role.ACTUATOR, false);
        memberRepository.save(MemberEntity.of(actuator, passwordEncoder));

        ReflectionTestUtils.setField(jwtAuthenticationFilter, "actuatorEmail", actuatorEmail);

        //when
        jwtAuthenticationFilter.init();

        //then
        assertThat(ReflectionTestUtils.getField(jwtAuthenticationFilter, "actuatorAuthenticationToken")).isNotNull();
        assertThat(((Authentication) ReflectionTestUtils.getField(jwtAuthenticationFilter, "actuatorAuthenticationToken")).getAuthorities()).isEmpty();
    }
}
