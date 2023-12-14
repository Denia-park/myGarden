package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
public class RoutineTime {
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public RoutineTime(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        validateConstructor(startDateTime, endDateTime);

        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void validateConstructor(final LocalDateTime startTime, final LocalDateTime endTime) {
        Assert.isTrue(startTime != null, "시작 시간은 null이 될 수 없습니다.");
        Assert.isTrue(endTime != null, "종료 시간은 null이 될 수 없습니다.");
        Assert.isTrue(startTime.isBefore(endTime), "시작 시간은 종료 시간보다 빨라야 합니다.");
    }

    public boolean isSameDate() {
        return startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate());
    }

    @Override
    public String toString() {
        return "RoutineTime{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
