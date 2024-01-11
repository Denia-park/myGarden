package org.hyunggi.mygardenbe.dailyroutine.service.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;

@Builder
public record DailyRoutineResponse(
        Long id,
        String startDateTime,
        String endDateTime,
        String routineType,
        String routineDescription) {
    public static DailyRoutineResponse of(DailyRoutine dailyRoutine) {
        return DailyRoutineResponse.builder()
                .startDateTime(dailyRoutine.getStartDateTimeString())
                .endDateTime(dailyRoutine.getEndDateTimeString())
                .routineType(dailyRoutine.getRoutineTypeDescription())
                .routineDescription(dailyRoutine.getRoutineDescription())
                .build();
    }

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
