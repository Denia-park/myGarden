package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API Controller Advice
 * <br><br>
 * - API Controller에서 발생하는 예외를 처리하는 Advice
 */
@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {
    /**
     * BindException 예외 처리
     *
     * @param e BindException
     * @return ApiResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(final BindException e) {
        final String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("API Controller BindException : {}", errorMessage);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null
        );
    }

    /**
     * EntityNotFoundException 예외 처리
     *
     * @param e EntityNotFoundException
     * @return ApiResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<Object> entityNotFoundException(final EntityNotFoundException e) {
        final String errorMessage = e.getMessage();
        log.warn("API Controller EntityNotFoundException : {}", errorMessage);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null
        );
    }

    /**
     * IllegalArgumentException 예외 처리
     *
     * @param e IllegalArgumentException
     * @return ApiResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> illegalArgumentException(final IllegalArgumentException e) {
        final String errorMessage = e.getMessage();
        log.warn("API Controller IllegalArgumentException : {}", errorMessage);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null
        );
    }

    /**
     * BusinessException 예외 처리
     *
     * @param e BusinessException
     * @return ApiResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> businessException(final BusinessException e) {
        final String errorMessage = e.getMessage();
        log.warn("API Controller BusinessException : {}", errorMessage);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null
        );
    }
}
