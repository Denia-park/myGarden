package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.Getter;

@Getter
public class DailyRoutine {
    private RoutineTime routineTime;
    private RoutineType routineType;
    private String routineDescription;

    public DailyRoutine(RoutineTime routineTime, String routineType, String routineDescription) {
        this.routineTime = routineTime;
        this.routineType = RoutineType.valueOf(routineType);
        this.routineDescription = routineDescription;
    }
}
