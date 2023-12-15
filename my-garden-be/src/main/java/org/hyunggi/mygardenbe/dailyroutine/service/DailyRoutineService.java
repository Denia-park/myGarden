package org.hyunggi.mygardenbe.dailyroutine.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyRoutineService {
    private final DailyRoutineRepository dailyRoutineRepository;

    @Transactional
    public void postDailyRoutine(final List<RoutineTime> routineTimes, final String routineType, final String routineDescription) {
        final List<DailyRoutine> dailyRoutines = convertDailyRoutines(routineTimes, routineType, routineDescription);

        dailyRoutineRepository.saveAll(dailyRoutines);
    }

    private List<DailyRoutine> convertDailyRoutines(final List<RoutineTime> routineTimes, final String routineType, final String routineDescription) {
        return routineTimes.stream()
                .map(routineTime -> new DailyRoutine(routineTime, routineType, routineDescription))
                .toList();
    }
}
