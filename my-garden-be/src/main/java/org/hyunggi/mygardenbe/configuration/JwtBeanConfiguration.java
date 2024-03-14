package org.hyunggi.mygardenbe.configuration;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Jwt Bean Configuration
 * <br><br>
 * - Jwt 관련 Bean 설정
 */
@RequiredArgsConstructor
@Configuration
public class JwtBeanConfiguration {
    /**
     * Member Entity Repository
     */
    private final MemberRepository memberRepository;

    /**
     * UserDetailsService Bean
     * <br><br>
     * - 전달받은 username으로 Member Entity를 조회
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    /**
     * PasswordEncoder Bean
     * <br><br>
     * - BCryptPasswordEncoder 사용 (비밀번호 암호화)
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager Bean
     * <br><br>
     * - default 설정된 authenticationManager를 사용
     *
     * @param config AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
