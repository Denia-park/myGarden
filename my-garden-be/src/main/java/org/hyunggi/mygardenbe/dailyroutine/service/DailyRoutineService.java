package org.hyunggi.mygardenbe.dailyroutine.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyRoutineService {
    private final DailyRoutineRepository dailyRoutineRepository;

    @Transactional
    public List<Long> postDailyRoutine(final List<RoutineTime> routineTimes, final String routineType, final String routineDescription) {
        final List<DailyRoutine> dailyRoutines = convertDailyRoutines(routineTimes, routineType, routineDescription);

        return extractIds(dailyRoutineRepository.saveAll(DailyRoutineEntity.of(dailyRoutines)));
    }

    private List<DailyRoutine> convertDailyRoutines(final List<RoutineTime> routineTimes, final String routineType, final String routineDescription) {
        return routineTimes.stream()
                .map(routineTime -> DailyRoutine.of(routineTime, routineType, routineDescription))
                .toList();
    }

    private List<Long> extractIds(final List<DailyRoutineEntity> savedDailyRoutines) {
        return savedDailyRoutines.stream()
                .map(DailyRoutineEntity::getId)
                .toList();
    }

    public List<DailyRoutineResponse> getDailyRoutine() {
        final List<DailyRoutineEntity> dailyRoutineEntities = dailyRoutineRepository.findAll();
        final List<DailyRoutine> dailyRoutines = convertDailyRoutines(dailyRoutineEntities);

        return convertDailyRoutineResponses(dailyRoutines);
    }

    private List<DailyRoutine> convertDailyRoutines(final List<DailyRoutineEntity> dailyRoutineEntities) {
        return dailyRoutineEntities.stream()
                .map(DailyRoutineEntity::toDomain)
                .toList();
    }

    private List<DailyRoutineResponse> convertDailyRoutineResponses(final List<DailyRoutine> dailyRoutines) {
        return dailyRoutines.stream()
                .sorted(this::compareStartDateTime)
                .map(DailyRoutineResponse::of)
                .toList();
    }

    private int compareStartDateTime(final DailyRoutine o1, final DailyRoutine o2) {
        return o1.getStartDateTime().compareTo(o2.getStartDateTime());
    }
}
