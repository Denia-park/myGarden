package org.hyunggi.mygardenbe.auth.jwt.service;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {
    public MyAuthenticationProvider(final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService) {
        super(passwordEncoder);

        super.setUserDetailsService(userDetailsService);
    }
}
