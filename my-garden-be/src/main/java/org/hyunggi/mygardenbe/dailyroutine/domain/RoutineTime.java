package org.hyunggi.mygardenbe.dailyroutine.domain;

import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class RoutineTime {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RoutineTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        validateConstructor(startTime, endTime);

        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateConstructor(final LocalDateTime startTime, final LocalDateTime endTime) {
        Assert.isTrue(startTime != null, "시작 시간은 null이 될 수 없습니다.");
        Assert.isTrue(endTime != null, "종료 시간은 null이 될 수 없습니다.");
        Assert.isTrue(startTime.isBefore(endTime), "시작 시간은 종료 시간보다 빨라야 합니다.");
    }
}
