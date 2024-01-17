package org.hyunggi.mygardenbe.dailyroutine.service.response;

import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DailyRoutineResponseTest {
    @Test
    @DisplayName("DailyRoutineResponse를 생성한다")
    void of() {
        //given
        DailyRoutine dailyRoutine = DailyRoutine.of(
                RoutineTime.of(
                        LocalDateTime.of(2021, 8, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 8, 1, 1, 0, 0)
                ),
                "SLEEP",
                "잠을 잔다"
        );

        //when
        DailyRoutineResponse dailyRoutineResponse = DailyRoutineResponse.of(dailyRoutine);

        //then
        assertThat(dailyRoutineResponse).isNotNull();
        assertThat(dailyRoutineResponse.id()).isNull();
        assertThat(dailyRoutineResponse.startDateTime()).isEqualTo("2021-08-01T00:00:00");
        assertThat(dailyRoutineResponse.endDateTime()).isEqualTo("2021-08-01T01:00:00");
        assertThat(dailyRoutineResponse.routineType()).isEqualTo("수면");
        assertThat(dailyRoutineResponse.routineDescription()).isEqualTo("잠을 잔다");
    }

    @Test
    void of_withId() {
        //given
        DailyRoutine dailyRoutine = DailyRoutine.of(
                RoutineTime.of(
                        LocalDateTime.of(2021, 8, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 8, 1, 1, 0, 0)
                ),
                "SLEEP",
                "잠을 잔다"
        );
        final long id = 1L;

        //when
        DailyRoutineResponse dailyRoutineResponse = DailyRoutineResponse.of(id, dailyRoutine);

        //then
        assertThat(dailyRoutineResponse).isNotNull();
        assertThat(dailyRoutineResponse.id()).isEqualTo(id);
        assertThat(dailyRoutineResponse.startDateTime()).isEqualTo("2021-08-01T00:00:00");
        assertThat(dailyRoutineResponse.endDateTime()).isEqualTo("2021-08-01T01:00:00");
        assertThat(dailyRoutineResponse.routineType()).isEqualTo("수면");
        assertThat(dailyRoutineResponse.routineDescription()).isEqualTo("잠을 잔다");
    }
}
