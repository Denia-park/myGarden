package org.hyunggi.mygardenbe.dailyroutine.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineTime {
    private static final int SECONDS_PER_DAY = 60 * 60 * 24;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private RoutineTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static RoutineTime of(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        validateConstructor(startDateTime, endDateTime);

        return new RoutineTime(startDateTime, endDateTime);
    }

    private static void validateConstructor(final LocalDateTime startTime, final LocalDateTime endTime) {
        Assert.isTrue(startTime != null, "시작 시간은 null이 될 수 없습니다.");
        Assert.isTrue(endTime != null, "종료 시간은 null이 될 수 없습니다.");

        if (startTime.isAfter(endTime)) {
            throw new BusinessException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        if (Duration.between(startTime, endTime).toSeconds() > SECONDS_PER_DAY) {
            throw new BusinessException("날짜는 하루 이상 차이날 수 없습니다.");
        }
    }

    public boolean isSameDate() {
        return getStartDate().isEqual(getEndDate());
    }

    public LocalDate getStartDate() {
        return startDateTime.toLocalDate();
    }

    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RoutineTime that = (RoutineTime) o;
        return Objects.equals(getStartDateTime(), that.getStartDateTime()) && Objects.equals(getEndDateTime(), that.getEndDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDateTime(), getEndDateTime());
    }

    @Override
    public String toString() {
        return "RoutineTime{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }

    public String getStartDateTimeString() {
        return startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getEndDateTimeString() {
        return endDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public boolean isNotStartAndEndDateEqualTo(final RoutineTime routineTime) {
        return !isStartAndEndDateEqualTo(routineTime);
    }

    public boolean isStartAndEndDateEqualTo(final RoutineTime routineTime) {
        return isSameStartDate(routineTime.getStartDate()) && isSameEndDate(routineTime.getEndDate());
    }

    private boolean isSameStartDate(final LocalDate startDate) {
        return getStartDate().isEqual(startDate);
    }

    private boolean isSameEndDate(final LocalDate endDate) {
        return getEndDate().isEqual(endDate);
    }

}
