package org.hyunggi.mygardenbe.mock.security;

import org.hyunggi.mygardenbe.member.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMyCustomUserSecurityContextFactory.class)
public @interface WithMyCustomUser {
    String email() default "test@test.com";

    Role role() default Role.USER;
}
