package org.hyunggi.mygardenbe.dailyroutine.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 루틴 시간
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineTime {
    /**
     * 하루를 초로 환산한 값
     */
    private static final int SECONDS_PER_DAY = 60 * 60 * 24;

    /**
     * 루틴 시작 시간
     */
    private LocalDateTime startDateTime;

    /**
     * 루틴 종료 시간
     */
    private LocalDateTime endDateTime;

    @Builder(access = AccessLevel.PRIVATE)
    private RoutineTime(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * 루틴 시간 생성
     *
     * @param startDateTime 루틴 시작 시간
     * @param endDateTime   루틴 종료 시간
     * @return RoutineTime Domain
     */
    public static RoutineTime of(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        validateConstructor(startDateTime, endDateTime);

        return RoutineTime.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

    /**
     * 루틴 시간 생성 인자 유효성 검증
     *
     * @param startTime 루틴 시작 시간
     * @param endTime   루틴 종료 시간
     */
    private static void validateConstructor(final LocalDateTime startTime, final LocalDateTime endTime) {
        Assert.isTrue(startTime != null, "시작 시간은 null이 될 수 없습니다.");
        Assert.isTrue(endTime != null, "종료 시간은 null이 될 수 없습니다.");

        if (startTime.isAfter(endTime)) {
            throw new BusinessException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        if (isDurationExceedingOneDay(startTime, endTime)) {
            throw new BusinessException("날짜는 하루 이상 차이날 수 없습니다.");
        }
    }

    /**
     * 루틴 시작 시간과 종료 시간의 시간 차이가 하루를 초과하는지 확인
     *
     * @param startTime 루틴 시작 시간
     * @param endTime   루틴 종료 시간
     * @return 하루를 초과하는지 여부
     */
    private static boolean isDurationExceedingOneDay(final LocalDateTime startTime, final LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toSeconds() > SECONDS_PER_DAY;
    }

    /**
     * 시작 날짜와 종료 날짜가 같은지 확인
     *
     * @return 시작 날짜과 종료 날짜가 같은지 여부
     */
    public boolean isSameDate() {
        return getStartDate().isEqual(getEndDate());
    }

    /**
     * 시작 날짜 조회
     *
     * @return 시작 날짜
     */
    public LocalDate getStartDate() {
        return startDateTime.toLocalDate();
    }

    /**
     * 종료 날짜 조회
     *
     * @return 종료 날짜
     */
    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    /**
     * equals 메서드 재정의
     *
     * @param o 비교 대상
     * @return 비교 결과
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RoutineTime that = (RoutineTime) o;
        return Objects.equals(getStartDateTime(), that.getStartDateTime()) && Objects.equals(getEndDateTime(), that.getEndDateTime());
    }

    /**
     * hashCode 메서드 재정의
     *
     * @return 해시 코드
     */
    @Override
    public int hashCode() {
        return Objects.hash(getStartDateTime(), getEndDateTime());
    }

    /**
     * toString 메서드 재정의
     *
     * @return 문자열
     */
    @Override
    public String toString() {
        return "RoutineTime{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }

    /**
     * 시작 시간 문자열 조회
     *
     * @return 시작 시간 문자열
     */
    public String getStartDateTimeString() {
        return startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * 종료 시간 문자열 조회
     *
     * @return 종료 시간 문자열
     */
    public String getEndDateTimeString() {
        return endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * 루틴 시간을 비교
     *
     * @param routineTime 비교할 루틴 시간
     * @return 두 루틴 시간이 다른지 여부
     */
    public boolean isNotStartAndEndDateEqualTo(final RoutineTime routineTime) {
        return !isStartAndEndDateEqualTo(routineTime);
    }

    /**
     * 시작 시간과 종료 시간이 같은지 확인
     *
     * @param routineTime 비교할 루틴 시간
     * @return 시작 시간과 종료 시간이 같은지 여부
     */
    public boolean isStartAndEndDateEqualTo(final RoutineTime routineTime) {
        return isSameStartDate(routineTime.getStartDate()) && isSameEndDate(routineTime.getEndDate());
    }

    /**
     * 시작 날짜가 같은지 확인
     *
     * @param startDate 비교할 시작 날짜
     * @return 시작 날짜가 같은지 여부
     */
    private boolean isSameStartDate(final LocalDate startDate) {
        return getStartDate().isEqual(startDate);
    }

    /**
     * 종료 날짜가 같은지 확인
     *
     * @param endDate 비교할 종료 날짜
     * @return 종료 날짜가 같은지 여부
     */
    private boolean isSameEndDate(final LocalDate endDate) {
        return getEndDate().isEqual(endDate);
    }
}
