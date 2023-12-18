package org.hyunggi.mygardenbe.dailyroutine.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GetRequest(
        @NotNull(message = "데일리 루틴의 시작 시간은 null이 될 수 없습니다.")
        LocalDateTime startDateTime,
        @NotNull(message = "데일리 루틴의 종료 시간은 null이 될 수 없습니다.")
        LocalDateTime endDateTime) {
}
