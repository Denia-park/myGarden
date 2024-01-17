package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import jakarta.persistence.EntityNotFoundException;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

import static org.assertj.core.api.Assertions.assertThat;

class ApiControllerAdviceTest {
    private ApiControllerAdvice apiControllerAdvice;

    @BeforeEach
    void setUp() {
        apiControllerAdvice = new ApiControllerAdvice();
    }

    @Test
    @DisplayName("bindException 메서드를 호출하면, 400 Bad Request 응답을 반환한다.")
    void bindException() {
        //given
        int sampleInteger = 10;
        final BindException bindException = new BindException(sampleInteger, "sampleInteger");
        bindException.reject("sampleInteger", "sampleInteger");

        //when
        final ApiResponse<Object> objectApiResponse = apiControllerAdvice.bindException(bindException);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(objectApiResponse.getMessage()).isEqualTo("sampleInteger");
        assertThat(objectApiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("entityNotFoundException 메서드를 호출하면, 400 Bad Request 응답을 반환한다.")
    void entityNotFoundException() {
        //given
        final String sampleMessage = "sampleMessage";
        final EntityNotFoundException entityNotFoundException = new EntityNotFoundException(sampleMessage);

        //when
        final ApiResponse<Object> objectApiResponse = apiControllerAdvice.entityNotFoundException(entityNotFoundException);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(objectApiResponse.getMessage()).isEqualTo(sampleMessage);
        assertThat(objectApiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("illegalArgumentException 메서드를 호출하면, 400 Bad Request 응답을 반환한다.")
    void illegalArgumentException() {
        //given
        final String sampleMessage = "sampleMessage";
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(sampleMessage);

        //when
        final ApiResponse<Object> objectApiResponse = apiControllerAdvice.illegalArgumentException(illegalArgumentException);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(objectApiResponse.getMessage()).isEqualTo(sampleMessage);
        assertThat(objectApiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("businessException 메서드를 호출하면, 400 Bad Request 응답을 반환한다.")
    void businessException() {
        //given
        final String sampleMessage = "sampleMessage";
        final BusinessException businessException = new BusinessException(sampleMessage);

        //when
        final ApiResponse<Object> objectApiResponse = apiControllerAdvice.businessException(businessException);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(objectApiResponse.getMessage()).isEqualTo(sampleMessage);
        assertThat(objectApiResponse.getData()).isNull();
    }
}
