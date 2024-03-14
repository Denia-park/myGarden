package org.hyunggi.mygardenbe.common.exception;

/**
 * Business Exception
 * <br><br>
 * - 비즈니스 로직에서 발생하는 예외
 */
public class BusinessException extends RuntimeException {
    public BusinessException(final String message) {
        super(message);
    }
}
