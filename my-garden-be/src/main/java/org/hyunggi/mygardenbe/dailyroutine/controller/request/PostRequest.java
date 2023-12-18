package org.hyunggi.mygardenbe.dailyroutine.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PostRequest(
        @NotBlank(message = "데일리 루틴의 시작 시간은 비어있을 수 없습니다.")
        String startDateTime,
        @NotBlank(message = "데일리 루틴의 종료 시간은 비어있을 수 없습니다.")
        String endDateTime,
        @NotBlank(message = "데일리 루틴의 타입은 비어있을 수 없습니다.")
        String routineType,
        @NotNull(message = "데일리 루틴의 설명은 null이 될 수 없습니다.")
        String routineDescription
) {
}