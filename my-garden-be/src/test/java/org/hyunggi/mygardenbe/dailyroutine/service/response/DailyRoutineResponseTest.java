package org.hyunggi.mygardenbe.dailyroutine.service.response;

import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
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

    @Test
    @DisplayName("getStartDate()를 호출하면 DailyRoutine의 시작 날짜를 반환한다")
    void getStartDate() {
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
        assertThat(dailyRoutineResponse.getStartDate()).isEqualTo("2021-08-01");
    }

    @Test
    @DisplayName("isEqualType()를 호출하면 DailyRoutine의 루틴 타입이 같은지 여부를 반환한다")
    void isEqualType() {
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
        assertThat(dailyRoutineResponse.isEqualType(RoutineType.SLEEP)).isTrue();
    }

    @Test
    @DisplayName("calculateMinutesBetweenStartAndEnd()를 호출하면 DailyRoutine의 시작 시간과 종료 시간의 차이를 분으로 반환한다")
    void calculateMinutesBetweenStartAndEnd() {
        //given
        DailyRoutine dailyRoutine = DailyRoutine.of(
                RoutineTime.of(
                        LocalDateTime.of(2021, 8, 1, 0, 1, 0),
                        LocalDateTime.of(2021, 8, 1, 1, 12, 0)
                ),
                "SLEEP",
                "잠을 잔다"
        );

        //when
        DailyRoutineResponse dailyRoutineResponse = DailyRoutineResponse.of(dailyRoutine);

        //then
        assertThat(dailyRoutineResponse.calculateMinutesBetweenStartAndEnd()).isEqualTo(71);
    }
}
