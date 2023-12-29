package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoutineTimeTest {
    @Test
    @DisplayName("시작시간이 종료시간보다 느리면 예외가 발생한다.")
    void throwExceptionWhenStartTimeIsAfterThanEndTime() {
        //given
        final LocalDateTime startTime = LocalDateTime.of(2023, 12, 14, 9, 10, 0);
        final LocalDateTime endTime = LocalDateTime.of(2023, 12, 14, 9, 0, 0);

        //when, then
        assertThatThrownBy(() -> RoutineTime.of(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시작 시간은 종료 시간보다 빨라야 합니다.");
    }

    @ParameterizedTest
    @MethodSource("providerIsSameDate")
    @DisplayName("시작 날짜와 종료 날짜가 같은지 비교한다.")
    void isSameDate(RoutineTime routineTime, boolean expected) {
        //given, when
        final boolean isSameDate = routineTime.isSameDate();

        //then
        assertThat(isSameDate).isEqualTo(expected);
    }

    public static Stream<Arguments> providerIsSameDate() {
        return Stream.of(
                Arguments.of(
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
                        ),
                        true
                ),
                Arguments.of(
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 15, 9, 0, 0)
                        ),
                        false
                )
        );
    }

    @Test
    @DisplayName("만약 날짜가 하루이상 차이가 나면 예외가 발생한다")
    void throwExceptionWhenDateIsMoreThanOneDay() {
        //given
        final LocalDateTime startTime = LocalDateTime.of(2023, 12, 14, 9, 0, 0);
        final LocalDateTime endTime = LocalDateTime.of(2023, 12, 15, 9, 0, 1);

        //when, then
        assertThatThrownBy(() -> RoutineTime.of(startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("날짜는 하루 이상 차이날 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("providerIsStartAndEndDateEqualTo")
    @DisplayName("입력으로 전달한 날짜가 RoutineTime의 시작 날짜와 종료 날짜랑 같은지 비교한다.")
    void throwExceptionWhenInputDateIsDifferentFromRoutineTime(RoutineTime inputDate, boolean expected) {
        //given
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
        );

        //when
        final boolean isStartAndEndDateEqualTo = routineTime.isStartAndEndDateEqualTo(inputDate);

        //then
        assertThat(isStartAndEndDateEqualTo).isEqualTo(expected);
    }

    public static Stream<Arguments> providerIsStartAndEndDateEqualTo() {
        return Stream.of(
                Arguments.of(
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 14, 10, 0, 0),
                                LocalDateTime.of(2023, 12, 14, 11, 0, 0)
                        ),
                        true
                ),
                Arguments.of(
                        RoutineTime.of(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 15, 9, 0, 0)
                        ),
                        false
                )
        );
    }
}
