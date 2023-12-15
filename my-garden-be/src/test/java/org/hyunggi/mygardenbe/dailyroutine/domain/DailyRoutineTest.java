package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DailyRoutineTest {
    @Test
    @DisplayName("생성자를 통해 객체를 생성할 수 있다.")
    void constructor() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );
        final String routineType = "SLEEP";
        final String routineDescription = "낮잠";

        //when
        final DailyRoutine dailyRoutine = new DailyRoutine(
                routineTime,
                routineType,
                routineDescription
        );

        //then
        assertThat(dailyRoutine).isNotNull();
        assertThat(dailyRoutine.getStartDateTime()).isEqualTo(LocalDateTime.of(2023, 12, 14, 9, 0, 0));
        assertThat(dailyRoutine.getEndDateTime()).isEqualTo(LocalDateTime.of(2023, 12, 14, 10, 0, 0));
        assertThat(dailyRoutine.getRoutineType()).isEqualTo(RoutineType.SLEEP);
        assertThat(dailyRoutine.getRoutineDescription()).isEqualTo(routineDescription);
    }
}
