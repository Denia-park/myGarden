package org.hyunggi.mygardenbe.dailyroutine.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineTime {
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
        Assert.isTrue(startTime.isBefore(endTime), "시작 시간은 종료 시간보다 빨라야 합니다.");
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
}
