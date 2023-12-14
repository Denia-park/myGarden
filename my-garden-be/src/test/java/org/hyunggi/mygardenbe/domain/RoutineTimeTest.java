package org.hyunggi.mygardenbe.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
}
