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
        assertThatThrownBy(() -> new RoutineTime(startTime, endTime))
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
                        new RoutineTime(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 14, 10, 0, 0)
                        ),
                        true
                ),
                Arguments.of(
                        new RoutineTime(
                                LocalDateTime.of(2023, 12, 14, 9, 0, 0),
                                LocalDateTime.of(2023, 12, 15, 10, 0, 0)
                        ),
                        false
                )
        );
    }
}
