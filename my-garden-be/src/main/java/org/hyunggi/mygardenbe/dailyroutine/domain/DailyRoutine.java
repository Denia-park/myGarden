package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
public class DailyRoutine {
    private RoutineTime routineTime;
    private RoutineType routineType;
    private String routineDescription;

    public DailyRoutine(RoutineTime routineTime, String routineType, String routineDescription) {
        this(routineTime, RoutineType.valueOf(routineType), routineDescription);
    }

    public DailyRoutine(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        validateConstructor(routineTime, routineType, routineDescription);

        this.routineTime = routineTime;
        this.routineType = routineType;
        this.routineDescription = routineDescription;
    }

    private void validateConstructor(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        Assert.isTrue(routineTime != null, "데일리 루틴의 시간은 null이 될 수 없습니다.");
        Assert.isTrue(routineType != null, "데일리 루틴의 타입은 null이 될 수 없습니다.");
        Assert.isTrue(routineDescription != null, "데일리 루틴의 설명은 null이 될 수 없습니다.");
    }

    public LocalDateTime getStartDateTime() {
        return routineTime.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return routineTime.getEndDateTime();
    }
}
