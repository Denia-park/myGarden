package org.hyunggi.mygardenbe.dailyroutine.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<DailyRoutineResponse> getDailyRoutine(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        final List<DailyRoutineEntity> dailyRoutineEntities = dailyRoutineRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
        final Map<Long, DailyRoutine> dailyRoutines = convertMapWithKeyAndDailyRoutine(dailyRoutineEntities);

        return convertDailyRoutineResponses(dailyRoutines);
    }

    private Map<Long, DailyRoutine> convertMapWithKeyAndDailyRoutine(final List<DailyRoutineEntity> dailyRoutineEntities) {
        return dailyRoutineEntities.stream()
                .collect(Collectors.toMap(DailyRoutineEntity::getId, DailyRoutineEntity::toDomain));
    }

    private List<DailyRoutineResponse> convertDailyRoutineResponses(final Map<Long, DailyRoutine> dailyRoutines) {
        return dailyRoutines.entrySet().stream()
                .map(entry -> DailyRoutineResponse.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyRoutineResponse::startDateTime))
                .collect(Collectors.toList());
    }
}
