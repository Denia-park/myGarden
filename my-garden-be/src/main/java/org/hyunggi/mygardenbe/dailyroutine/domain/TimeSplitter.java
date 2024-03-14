package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 루틴 시간 분할
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TimeSplitter {
    /**
     * 루틴 시간 분할
     * <br><br>
     * - 루틴 시작 날짜와 종료 날짜가 같으면, 루틴 시간을 그대로 반환
     * - 루틴 시작 날짜와 종료 날짜가 다르면, 루틴 시간을 시작 날짜와 종료 날짜로 분할하여 반환 (Entity 관리의 편의성을 위해)
     *
     * @param routineTime 루틴 시간
     * @return 분할된 루틴 시간
     */
    public static List<RoutineTime> split(final RoutineTime routineTime) {
        if (routineTime.isSameDate()) {
            return List.of(routineTime);
        }

        return List.of(
                makeAnotherStartDateTime(routineTime),
                makeAnotherEndDateTime(routineTime)
        );
    }

    /**
     * 날짜가 다른 경우, 시작 시간은 그대로 사용하고 종료 시간은 시작 날짜의 마지막 시간으로 변경
     *
     * @param routineTime 루틴 시간
     * @return 날짜를 분할한 루틴 시간
     */
    private static RoutineTime makeAnotherStartDateTime(final RoutineTime routineTime) {
        final LocalDateTime originalStartTime = routineTime.getStartDateTime();
        final LocalDateTime dayEndDateTime = LocalDateTime.of(routineTime.getStartDate(), LocalTime.of(23, 59, 59));

        return RoutineTime.of(originalStartTime, dayEndDateTime);
    }

    /**
     * 날짜가 다른 경우, 종료 시간은 그대로 사용하고 시작 시간은 종료 날짜의 시작 시간으로 변경
     *
     * @param routineTime 루틴 시간
     * @return 날짜를 분할한 루틴 시간
     */
    private static RoutineTime makeAnotherEndDateTime(final RoutineTime routineTime) {
        final LocalDateTime dayStart = LocalDateTime.of(routineTime.getEndDate(), LocalTime.of(0, 0));
        final LocalDateTime originalEndTime = routineTime.getEndDateTime();

        return RoutineTime.of(dayStart, originalEndTime);
    }
}
