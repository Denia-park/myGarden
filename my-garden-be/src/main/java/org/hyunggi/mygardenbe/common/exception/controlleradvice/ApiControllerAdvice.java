package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        final String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("API Controller BindException : {}", errorMessage);

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null
        );
    }
}
