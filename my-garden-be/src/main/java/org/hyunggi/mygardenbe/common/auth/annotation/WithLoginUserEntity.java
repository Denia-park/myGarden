package org.hyunggi.mygardenbe.common.auth.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 로그인 유저 Entity 어노테이션
 * <br><br>
 * - 로그인 유저 Entity를 주입받기 위한 어노테이션
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface WithLoginUserEntity {
}
