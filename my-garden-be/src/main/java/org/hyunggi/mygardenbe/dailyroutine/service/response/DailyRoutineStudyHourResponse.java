package org.hyunggi.mygardenbe.dailyroutine.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 루틴의 공부 시간 조회 응답
 *
 * @param date      날짜
 * @param studyHour 해당 날짜의 공부 시간
 */
public record DailyRoutineStudyHourResponse(
        String date,
        @JsonProperty("count")
        Integer studyHour) {
    public DailyRoutineStudyHourResponse {
        validate(date, studyHour);
    }

    /**
     * 루틴의 공부 시간 조회 응답 유효성 검증
     */
    private static void validate(String date, Integer studyHour) {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
        if (studyHour == null || studyHour < 0) {
            throw new IllegalArgumentException("공부 시간은 null이 될 수 없고 0보다 커야 합니다.");
        }
    }
}
