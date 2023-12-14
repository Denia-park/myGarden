package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TimeSplitter {
    static List<RoutineTime> split(final RoutineTime routineTime) {
        if (routineTime.isSameDate()) {
            return List.of(routineTime);
        }

        return List.of(
                makeAnotherStartDateTime(routineTime),
                makeAnotherEndDateTime(routineTime)
        );
    }

    private static RoutineTime makeAnotherStartDateTime(final RoutineTime routineTime) {
        final LocalDateTime originalStartTime = routineTime.startDateTime();
        final LocalDateTime dayEndDateTime = LocalDateTime.of(routineTime.getStartDate(), LocalTime.of(23, 59, 59));

        return new RoutineTime(originalStartTime, dayEndDateTime);
    }

    private static RoutineTime makeAnotherEndDateTime(final RoutineTime routineTime) {
        final LocalDateTime dayStart = LocalDateTime.of(routineTime.getEndDate(), LocalTime.of(0, 0));
        final LocalDateTime originalEndTime = routineTime.endDateTime();

        return new RoutineTime(dayStart, originalEndTime);
    }
}
