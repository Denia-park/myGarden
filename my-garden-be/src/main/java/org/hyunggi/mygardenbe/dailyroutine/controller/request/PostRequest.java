package org.hyunggi.mygardenbe.dailyroutine.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 데일리 루틴 등록 요청
 *
 * @param startDateTime      루틴 시작 시간
 * @param endDateTime        루틴 종료 시간
 * @param routineType        루틴 타입
 * @param routineDescription 루틴 설명
 */
@Builder
public record PostRequest(
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
