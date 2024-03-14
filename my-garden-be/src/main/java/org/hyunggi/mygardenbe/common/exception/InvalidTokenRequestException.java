package org.hyunggi.mygardenbe.common.exception;

/**
 * Invalid Token Request Exception
 * <br><br>
 * - 토큰 요청이 유효하지 않을 때 발생하는 예외
 */
public class InvalidTokenRequestException extends RuntimeException {
    public InvalidTokenRequestException(String message) {
        super(message);
    }
}
