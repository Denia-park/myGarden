package org.hyunggi.mygardenbe.common.exception;

public class InvalidTokenRequestException extends RuntimeException {
    public InvalidTokenRequestException(String message) {
        super(message);
    }
}
