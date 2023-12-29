package org.hyunggi.mygardenbe.dailyroutine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class DailyRoutineEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private RoutineTime routineTime;
    @Enumerated(EnumType.STRING)
    private RoutineType routineType;
    private String routineDescription;

    private DailyRoutineEntity(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        this.routineTime = routineTime;
        this.routineType = routineType;
        this.routineDescription = routineDescription;
    }

    public static DailyRoutineEntity of(DailyRoutine dailyRoutine) {
        return new DailyRoutineEntity(
                dailyRoutine.getRoutineTime(),
                dailyRoutine.getRoutineType(),
                dailyRoutine.getRoutineDescription()
        );
    }

    public static List<DailyRoutineEntity> of(final List<DailyRoutine> dailyRoutines) {
        return dailyRoutines.stream()
                .map(DailyRoutineEntity::of)
                .toList();
    }

    public DailyRoutine toDomain() {
        return DailyRoutine.of(
                routineTime,
                routineType,
                routineDescription
        );
    }

    public void update(final DailyRoutine dailyRoutine) {
        this.routineTime = dailyRoutine.getRoutineTime();
        this.routineType = dailyRoutine.getRoutineType();
        this.routineDescription = dailyRoutine.getRoutineDescription();
    }
}
