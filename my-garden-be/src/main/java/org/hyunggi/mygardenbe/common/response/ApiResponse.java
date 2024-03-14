package org.hyunggi.mygardenbe.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 응답
 * <br><br>
 * - API 응답을 위한 클래스
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
public class ApiResponse<T> {
    /**
     * Http 응답 코드
     */
    private final int code;

    /**
     * Http 응답 상태
     */
    private final HttpStatus status;

    /**
     * 응답 메시지
     */
    private final String message;

    /**
     * 응답 데이터
     */
    private final T data;

    private ApiResponse(final HttpStatus status, final String message, final T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 성공 응답 - 데이터 없음
     *
     * @param <T> 응답 데이터 타입
     * @return ApiResponse - 데이터 없음
     */
    public static <T> ApiResponse<T> ok() {
        return ok(null);
    }

    /**
     * 성공 응답
     *
     * @param data 응답 데이터
     * @param <T>  응답 데이터 타입
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> ok(final T data) {
        return of(HttpStatus.OK, data);
    }

    /**
     * 응답 생성
     *
     * @param httpStatus Http 응답 상태
     * @param data       응답 데이터
     * @param <T>        응답 데이터 타입
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(final HttpStatus httpStatus, final T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    /**
     * 응답 생성
     *
     * @param httpStatus Http 응답 상태
     * @param message    응답 메시지
     * @param data       응답 데이터
     * @param <T>        응답 데이터 타입
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(final HttpStatus httpStatus, final String message, final T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }
}
