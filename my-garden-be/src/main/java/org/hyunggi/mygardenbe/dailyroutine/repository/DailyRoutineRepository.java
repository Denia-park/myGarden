package org.hyunggi.mygardenbe.dailyroutine.repository;

import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutineEntity, Long> {
    @Query("select d from DailyRoutineEntity d where :startDateTime <= d.routineTime.startDateTime and d.routineTime.endDateTime <= :endDateTime")
    List<DailyRoutineEntity> findAllByDateTimeBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime);
}
