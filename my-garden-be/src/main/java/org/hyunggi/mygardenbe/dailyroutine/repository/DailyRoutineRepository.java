package org.hyunggi.mygardenbe.dailyroutine.repository;

import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutine, Long> {
}
