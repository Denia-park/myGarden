package org.hyunggi.mygardenbe.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    @DisplayName("정적 메서드 ok를 통해 ApiResponse 객체를 생성할 수 있고, HttpStatus Code는 200이며 data는 null이다.")
    void ok() {
        //given, when
        final ApiResponse<Object> objectApiResponse = ApiResponse.ok();

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getCode()).isEqualTo(200);
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(objectApiResponse.getMessage()).isEqualTo("OK");
        assertThat(objectApiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("정적 메서드 ok를 통해 ApiResponse 객체를 생성할 수 있고, HttpStatus Code는 200이며 data는 추가한 객체이다.")
    void ok_withData() {
        //given
        final Object sampleObject = new Object();

        //when
        final ApiResponse<Object> objectApiResponse = ApiResponse.ok(sampleObject);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getCode()).isEqualTo(200);
        assertThat(objectApiResponse.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(objectApiResponse.getMessage()).isEqualTo("OK");
        assertThat(objectApiResponse.getData()).isEqualTo(sampleObject);
    }

    @Test
    @DisplayName("정적 메서드 of를 통해 ApiResponse 객체를 생성할 수 있고," +
            " HttpStatus Code는 추가한 상태 값이며 상태 메세지도 추가한 메세지이고 data도 추가한 객체이다.")
    void of() {
        //given
        final HttpStatus sampleHttpStatus = HttpStatus.BAD_REQUEST;
        final String sampleMessage = "sampleMessage";
        final Object sampleObject = new Object();

        //when
        final ApiResponse<Object> objectApiResponse = ApiResponse.of(sampleHttpStatus, sampleMessage, sampleObject);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getCode()).isEqualTo(400);
        assertThat(objectApiResponse.getStatus()).isEqualTo(sampleHttpStatus);
        assertThat(objectApiResponse.getMessage()).isEqualTo(sampleMessage);
        assertThat(objectApiResponse.getData()).isEqualTo(sampleObject);
    }

    @Test
    @DisplayName("정적 메서드 of를 통해 ApiResponse 객체를 생성할 수 있고, HttpStatus Code는 추가한 상태 값이며 data는 추가한 객체이다.")
    void of_withoutMessage() {
        //given
        final HttpStatus sampleHttpStatus = HttpStatus.BAD_REQUEST;
        final Object sampleObject = new Object();

        //when
        final ApiResponse<Object> objectApiResponse = ApiResponse.of(sampleHttpStatus, sampleObject);

        //then
        assertThat(objectApiResponse).isNotNull();
        assertThat(objectApiResponse.getCode()).isEqualTo(400);
        assertThat(objectApiResponse.getStatus()).isEqualTo(sampleHttpStatus);
        assertThat(objectApiResponse.getMessage()).isEqualTo("BAD_REQUEST");
        assertThat(objectApiResponse.getData()).isEqualTo(sampleObject);
    }
}
