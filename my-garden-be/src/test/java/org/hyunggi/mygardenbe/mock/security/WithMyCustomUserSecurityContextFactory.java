package org.hyunggi.mygardenbe.mock.security;

import org.hyunggi.mygardenbe.member.domain.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMyCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMyCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMyCustomUser annotation) {
        String email = annotation.email();
        Role role = annotation.role();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                email,
                "",
                buildGrantedAuthoritiesFromRole(role)
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);

        return context;
    }

    private List<GrantedAuthority> buildGrantedAuthoritiesFromRole(final Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
