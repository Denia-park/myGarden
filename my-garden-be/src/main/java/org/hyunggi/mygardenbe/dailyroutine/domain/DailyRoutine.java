package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.Getter;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 데일리 루틴 Domain
 */
@Getter
public class DailyRoutine {
    /**
     * 루틴 시간
     */
    private RoutineTime routineTime;

    /**
     * 루틴 타입
     */
    private RoutineType routineType;

    /**
     * 루틴 설명
     */
    private String routineDescription;

    private DailyRoutine(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        validateConstructor(routineTime, routineType, routineDescription);

        this.routineTime = routineTime;
        this.routineType = routineType;
        this.routineDescription = routineDescription;
    }

    /**
     * 데일리 루틴 생성 인자 유효성 검증
     *
     * @param routineTime        루틴 시간
     * @param routineType        루틴 타입
     * @param routineDescription 루틴 설명
     */
    private void validateConstructor(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        Assert.isTrue(routineTime != null, "데일리 루틴의 시간은 null이 될 수 없습니다.");
        Assert.isTrue(routineType != null, "데일리 루틴의 타입은 null이 될 수 없습니다.");
        Assert.isTrue(routineDescription != null, "데일리 루틴의 설명은 null이 될 수 없습니다.");
    }

    /**
     * 데일리 루틴 생성
     *
     * @param routineTime        루틴 시간
     * @param routineType        루틴 타입
     * @param routineDescription 루틴 설명
     * @return DailyRoutine Domain
     */
    public static DailyRoutine of(final RoutineTime routineTime, final String routineType, final String routineDescription) {
        validateConstructor(routineType);
        return of(routineTime, RoutineType.valueOf(routineType), routineDescription);
    }

    /**
     * 루틴 타입 유효성 검증
     *
     * @param routineType 루틴 타입
     */
    private static void validateConstructor(final String routineType) {
        Assert.hasText(routineType, "데일리 루틴의 타입은 비어있을 수 없습니다.");
    }

    /**
     * 데일리 루틴 생성
     *
     * @param routineTime        루틴 시간
     * @param routineType        루틴 타입
     * @param routineDescription 루틴 설명
     * @return DailyRoutine Domain
     */
    public static DailyRoutine of(final RoutineTime routineTime, final RoutineType routineType, final String routineDescription) {
        return new DailyRoutine(routineTime, routineType, routineDescription);
    }

    /**
     * 루틴 시작 시간 조회
     *
     * @return 루틴 시작 시간
     */
    public LocalDateTime getStartDateTime() {
        return routineTime.getStartDateTime();
    }

    /**
     * 루틴 종료 시간 조회
     *
     * @return 루틴 종료 시간
     */
    public LocalDateTime getEndDateTime() {
        return routineTime.getEndDateTime();
    }

    /**
     * 루틴 시작 시간 문자열 조회
     *
     * @return 루틴 시작 시간 문자열
     */
    public String getStartDateTimeString() {
        return routineTime.getStartDateTimeString();
    }

    /**
     * 루틴 종료 시간 문자열 조회
     *
     * @return 루틴 종료 시간 문자열
     */
    public String getEndDateTimeString() {
        return routineTime.getEndDateTimeString();
    }

    /**
     * 루틴 타입 조회
     *
     * @return 루틴 타입
     */
    public String getRoutineTypeDescription() {
        return routineType.getDescription();
    }

    /**
     * 루틴 수정
     *
     * @param routineTime 루틴 시간
     * @param routineType 루틴 타입
     * @param description 루틴 설명
     */
    public void update(final RoutineTime routineTime, final RoutineType routineType, final String description) {
        validate(routineTime);

        this.routineTime = routineTime;
        this.routineType = routineType;
        this.routineDescription = description;
    }

    /**
     * 루틴 시간 유효성 검증
     *
     * @param routineTime 루틴 시간
     */
    private void validate(final RoutineTime routineTime) {
        if (this.routineTime.isNotStartAndEndDateEqualTo(routineTime)) {
            throw new BusinessException("동일한 날짜의 시간으로만 수정할 수 있습니다.");
        }
    }
}
