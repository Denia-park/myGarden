package org.hyunggi.mygardenbe.dailyroutine.repository;

import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutineEntity, Long> {
}
