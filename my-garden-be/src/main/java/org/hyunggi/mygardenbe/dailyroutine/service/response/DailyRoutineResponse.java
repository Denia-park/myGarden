package org.hyunggi.mygardenbe.dailyroutine.service.response;

import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;

public record DailyRoutineResponse(
        Long id,
        String startDateTime,
        String endDateTime,
        String routineType,
        String routineDescription) {
    public static DailyRoutineResponse of(DailyRoutine dailyRoutine) {
        return new DailyRoutineResponse(
                null,
                dailyRoutine.getStartDateTimeString(),
                dailyRoutine.getEndDateTimeString(),
                dailyRoutine.getRoutineTypeDescription(),
                dailyRoutine.getRoutineDescription()
        );
    }

    public static DailyRoutineResponse of(final Long id, final DailyRoutine dailyRoutine) {
        return new DailyRoutineResponse(
                id,
                dailyRoutine.getStartDateTimeString(),
                dailyRoutine.getEndDateTimeString(),
                dailyRoutine.getRoutineTypeDescription(),
                dailyRoutine.getRoutineDescription()
        );
    }
}
