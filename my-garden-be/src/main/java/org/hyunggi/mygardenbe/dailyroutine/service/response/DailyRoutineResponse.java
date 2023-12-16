package org.hyunggi.mygardenbe.dailyroutine.service.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;

@Builder
public record DailyRoutineResponse(
        String startDateTime,
        String endDateTime,
        String routineType,
        String routineDescription) {
    public static DailyRoutineResponse of(DailyRoutine dailyRoutine) {
        return new DailyRoutineResponse(
                dailyRoutine.getStartDateTimeString(),
                dailyRoutine.getEndDateTimeString(),
                dailyRoutine.getRoutineTypeDescription(),
                dailyRoutine.getRoutineDescription()
        );
    }
}
