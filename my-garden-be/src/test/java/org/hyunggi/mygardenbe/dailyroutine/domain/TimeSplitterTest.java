package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimeSplitterTest {
    @Test
    @DisplayName("시작 날짜와 종료 날짜가 같은 경우, 쪼개지지 않고 하나의 데일리 루틴을 반환한다.")
    void split_sameDate() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );

        //when
        final List<RoutineTime> split = TimeSplitter.split(routineTime);

        //then
        assertThat(split).hasSize(1)
                .containsExactly(routineTime);
    }

    @Test
    @DisplayName("시작 날짜와 종료 날짜가 다른 경우, 각 날짜로 분할하여 반환한다.")
    void split_anotherDate() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 15, 3, 0, 0)
        );

        //when
        final List<RoutineTime> split = TimeSplitter.split(routineTime);

        //then
        assertThat(split).hasSize(2)
                .containsExactly(
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 14, 23, 59, 59)
                        ),
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 15, 0, 0, 0),
                                LocalDateTime.of(2023, 12, 15, 3, 0, 0)
                        )
                );
    }
}
