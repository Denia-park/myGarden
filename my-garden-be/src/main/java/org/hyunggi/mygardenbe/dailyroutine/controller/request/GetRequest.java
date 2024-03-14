package org.hyunggi.mygardenbe.dailyroutine.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 데일리 루틴 조회 요청
 *
 * @param startDateTime 조회 시작 시간
 * @param endDateTime   조회 종료 시간
 */
public record GetRequest(
        @NotNull(message = "데일리 루틴의 시작 시간은 null이 될 수 없습니다.")
        LocalDateTime startDateTime,
        @NotNull(message = "데일리 루틴의 종료 시간은 null이 될 수 없습니다.")
        LocalDateTime endDateTime) {
}
