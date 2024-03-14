package org.hyunggi.mygardenbe.dailyroutine.repository;

import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 데일리 루틴 Entity Repository
 */
public interface DailyRoutineRepository extends JpaRepository<DailyRoutineEntity, Long> {
    /**
     * 데일리 루틴 Entity를 시간으로 조회
     *
     * @param startDateTime 조회 시작 시간
     * @param endDateTime   조회 종료 시간
     * @param memberId      멤버 ID
     * @return 데일리 루틴 Entity List
     */
    @Query("select d from DailyRoutineEntity d where :startDateTime <= d.routineTime.startDateTime and d.routineTime.endDateTime <= :endDateTime and d.memberId = :memberId")
    List<DailyRoutineEntity> findAllByDateTimeBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final Long memberId);
}
