package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RoutineTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    public RoutineTime {
        validateConstructor(startDateTime, endDateTime);
    }

    private void validateConstructor(final LocalDateTime startTime, final LocalDateTime endTime) {
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
    public String toString() {
        return "RoutineTime{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
