package org.hyunggi.mygardenbe.dailyroutine.service.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyRoutineStudyHourResponseTest {
    @Test
    @DisplayName("DailyRoutineStudyHourResponse 생성 테스트")
    void createDailyRoutineStudyHourResponse() {
        // given
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final int studyHour = 3;

        // when
        final DailyRoutineStudyHourResponse response = new DailyRoutineStudyHourResponse(date, studyHour);

        // then
        assertThat(response).isNotNull();
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.studyHour()).isEqualTo(studyHour);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("DailyRoutineStudyHourResponse 생성 테스트 - date가 null인 경우")
    void createDailyRoutineStudyHourResponseWithNullDate(String date) {
        // given
        final int studyHour = 3;

        // when, then
        assertThatThrownBy(() -> new DailyRoutineStudyHourResponse(date, studyHour))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("날짜는 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"-1", ","})
    @DisplayName("DailyRoutineStudyHourResponse 생성 테스트 - studyHour가 0보다 작거나 null인 경우")
    void createDailyRoutineStudyHourResponseWithInvalidStudyHour(Integer studyHour) {
        // given
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // when, then
        assertThatThrownBy(() -> new DailyRoutineStudyHourResponse(date, studyHour))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("공부 시간은 null이 될 수 없고 0보다 커야 합니다.");
    }
}
