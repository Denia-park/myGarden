package org.hyunggi.mygardenbe.dailyroutine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 데일리 루틴 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class DailyRoutineEntity extends BaseEntity {
    /**
     * 데일리 루틴 ID
     */
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    /**
     * 루틴 시간
     */
    @Embedded
    private RoutineTime routineTime;

    /**
     * 루틴 타입
     */
    @Enumerated(EnumType.STRING)
    private RoutineType routineType;

    /**
     * 루틴 설명
     */
    private String routineDescription;

    /**
     * 멤버 ID
     */
    @Column(nullable = false)
    private Long memberId;

    private DailyRoutineEntity(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription, final Long memberId) {
        this.routineTime = routineTime;
        this.routineType = routineType;
        this.routineDescription = routineDescription;
        this.memberId = memberId;
    }

    /**
     * 데일리 루틴 생성
     *
     * @param dailyRoutine 데일리 루틴 Domain
     * @param memberId     멤버 ID
     * @return 데일리 루틴 Entity
     */
    public static DailyRoutineEntity of(final DailyRoutine dailyRoutine, final Long memberId) {
        Assert.isTrue(dailyRoutine != null, "데일리 루틴은 null이 될 수 없습니다.");
        Assert.isTrue(memberId != null && memberId > 0, "멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");

        return new DailyRoutineEntity(
                dailyRoutine.getRoutineTime(),
                dailyRoutine.getRoutineType(),
                dailyRoutine.getRoutineDescription(),
                memberId
        );
    }

    /**
     * 데일리 루틴 목록 생성
     *
     * @param dailyRoutines 데일리 루틴 Domain 목록
     * @param memberId      멤버 ID
     * @return 데일리 루틴 Entity 목록
     */
    public static List<DailyRoutineEntity> of(final List<DailyRoutine> dailyRoutines, final Long memberId) {
        Assert.isTrue(dailyRoutines != null, "데일리 루틴은 null이 될 수 없습니다.");

        return dailyRoutines.stream()
                .map(dailyRoutine -> of(dailyRoutine, memberId))
                .toList();
    }

    /**
     * 데일리 루틴 Entity를 Domain으로 변환
     *
     * @return 데일리 루틴 Domain
     */
    public DailyRoutine toDomain() {
        return DailyRoutine.of(
                routineTime,
                routineType,
                routineDescription
        );
    }

    /**
     * 데일리 루틴 수정
     *
     * @param dailyRoutine 데일리 루틴 Domain
     */
    public void update(final DailyRoutine dailyRoutine) {
        this.routineTime = dailyRoutine.getRoutineTime();
        this.routineType = dailyRoutine.getRoutineType();
        this.routineDescription = dailyRoutine.getRoutineDescription();
    }

    /**
     * 해당 멤버가 데일리 루틴의 소유자인지 확인
     *
     * @param memberId 멤버 ID
     * @return 데일리 루틴 소유자 여부
     */
    public boolean isNotOwner(final Long memberId) {
        return !this.memberId.equals(memberId);
    }
}
