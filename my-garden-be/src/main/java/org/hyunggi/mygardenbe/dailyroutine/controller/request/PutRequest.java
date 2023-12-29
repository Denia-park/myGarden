package org.hyunggi.mygardenbe.dailyroutine.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PutRequest(
        @NotNull(message = "데일리 루틴의 ID는 null이 될 수 없습니다.")
        Long id,
        @NotBlank(message = "데일리 루틴의 시작 시간은 비어있을 수 없습니다.")
        String startDateTime,
        @NotBlank(message = "데일리 루틴의 종료 시간은 비어있을 수 없습니다.")
        String endDateTime,
        @NotBlank(message = "데일리 루틴의 타입은 비어있을 수 없습니다.")
        String routineType,
        @NotNull(message = "데일리 루틴의 설명은 null이 될 수 없습니다.")
        @Size(max = 255, message = "데일리 루틴의 설명은 255자를 넘을 수 없습니다.")
        String routineDescription
) {
}