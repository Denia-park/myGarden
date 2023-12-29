package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyRoutineTest {
    @Test
    @DisplayName("정적 메서드 of를 통해 객체를 생성할 수 있다.")
    void constructor() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );
        final String routineType = "SLEEP";
        final String routineDescription = "낮잠";

        //when
        final DailyRoutine dailyRoutine = DailyRoutine.of(
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

    @Test
    @DisplayName("update 메서드를 통해 객체를 수정할 수 있다.")
    void update() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );
        final RoutineType routineType = RoutineType.SLEEP;
        final String routineDescription = "낮잠";
        final DailyRoutine dailyRoutine = DailyRoutine.of(
                routineTime,
                routineType,
                routineDescription
        );

        //when
        final RoutineTime newRoutineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 10, 0, 0),
                LocalDateTime.of(2023, 12, 14, 11, 0, 0)
        );
        final RoutineType newRoutineType = RoutineType.STUDY;
        final String newRoutineDescription = "자바 스터디";
        dailyRoutine.update(
                newRoutineTime,
                newRoutineType,
                newRoutineDescription
        );

        //then
        assertThat(dailyRoutine).isNotNull();
        assertThat(dailyRoutine.getStartDateTime()).isEqualTo(LocalDateTime.of(2023, 12, 14, 10, 0, 0));
        assertThat(dailyRoutine.getEndDateTime()).isEqualTo(LocalDateTime.of(2023, 12, 14, 11, 0, 0));
        assertThat(dailyRoutine.getRoutineType()).isEqualTo(RoutineType.STUDY);
        assertThat(dailyRoutine.getRoutineDescription()).isEqualTo(newRoutineDescription);
    }

    @Test
    @DisplayName("update 메서드를 통해 객체를 수정할 때, 기존 데이터 날짜와 업데이트 데이터 날짜가 다르면 예외가 발생한다.")
    void throwExceptionWhenUpdateDailyRoutineIsNotToday() {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );
        final RoutineType routineType = RoutineType.SLEEP;
        final String routineDescription = "낮잠";
        final DailyRoutine dailyRoutine = DailyRoutine.of(
                routineTime,
                routineType,
                routineDescription
        );

        //when, then
        final RoutineTime newRoutineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 15, 10, 0, 0),
                LocalDateTime.of(2023, 12, 15, 11, 0, 0)
        );
        final RoutineType newRoutineType = RoutineType.STUDY;
        final String newRoutineDescription = "자바 스터디";
        assertThatThrownBy(() -> dailyRoutine.update(
                newRoutineTime,
                newRoutineType,
                newRoutineDescription
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("동일한 날짜의 시간으로만 수정할 수 있습니다.");
    }
}
