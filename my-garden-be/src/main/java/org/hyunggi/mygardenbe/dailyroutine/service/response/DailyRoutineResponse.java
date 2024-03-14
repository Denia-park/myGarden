package org.hyunggi.mygardenbe.dailyroutine.service.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;

/**
 * 데일리 루틴 조회 응답
 *
 * @param id                 루틴 ID
 * @param startDateTime      루틴 시작 시간
 * @param endDateTime        루틴 종료 시간
 * @param routineType        루틴 타입
 * @param routineDescription 루틴 설명
 */
@Builder
public record DailyRoutineResponse(
        Long id,
        String startDateTime,
        String endDateTime,
        String routineType,
        String routineDescription) {

    /**
     * 데일리 루틴 Domain -> 데일리 루틴 조회 응답 변환
     *
     * @param dailyRoutine 데일리 루틴 Domain
     * @return 데일리 루틴 조회 응답
     */
    public static DailyRoutineResponse of(DailyRoutine dailyRoutine) {
        return DailyRoutineResponse.builder()
                .startDateTime(dailyRoutine.getStartDateTimeString())
                .endDateTime(dailyRoutine.getEndDateTimeString())
                .routineType(dailyRoutine.getRoutineTypeDescription())
                .routineDescription(dailyRoutine.getRoutineDescription())
                .build();
    }

    /**
     * 데일리 루틴 Domain -> 데일리 루틴 조회 응답 변환 (ID 포함)
     *
     * @param id           루틴 ID
     * @param dailyRoutine 데일리 루틴 Domain
     * @return 데일리 루틴 조회 응답
     */
    public static DailyRoutineResponse of(final Long id, final DailyRoutine dailyRoutine) {
        return DailyRoutineResponse.builder()
                .id(id)
                .startDateTime(dailyRoutine.getStartDateTimeString())
                .endDateTime(dailyRoutine.getEndDateTimeString())
                .routineType(dailyRoutine.getRoutineTypeDescription())
                .routineDescription(dailyRoutine.getRoutineDescription())
                .build();
    }
}
