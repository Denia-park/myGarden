package org.hyunggi.mygardenbe.dailyroutine.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class DailyRoutine {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private RoutineTime routineTime;
    @Enumerated(EnumType.STRING)
    private RoutineType routineType;
    private String routineDescription;

    public DailyRoutine(RoutineTime routineTime, String routineType, String routineDescription) {
        validateConstructor(routineTime, routineType, routineDescription);

        this.routineTime = routineTime;
        this.routineType = RoutineType.valueOf(routineType);
        this.routineDescription = routineDescription;
    }

    private void validateConstructor(final RoutineTime routineTime, final String routineType, final String routineDescription) {
        Assert.isTrue(routineTime != null, "데일리 루틴의 시간은 null이 될 수 없습니다.");
        Assert.hasText(routineType, "데일리 루틴의 타입은 비어있을 수 없습니다.");
        Assert.isTrue(routineDescription != null, "데일리 루틴의 설명은 null이 될 수 없습니다.");
    }

    public LocalDateTime getStartDateTime() {
        return routineTime.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return routineTime.getEndDateTime();
    }
}
