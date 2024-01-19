package org.hyunggi.mygardenbe.dailyroutine.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
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
    public List<Long> postDailyRoutine(final List<RoutineTime> routineTimes, final RoutineType routineType, final String routineDescription, final MemberEntity member) {
        final List<DailyRoutine> dailyRoutines = convertDailyRoutines(routineTimes, routineType, routineDescription);

        return extractIds(dailyRoutineRepository.saveAll(DailyRoutineEntity.of(dailyRoutines, member.getId())));
    }

    private List<DailyRoutine> convertDailyRoutines(final List<RoutineTime> routineTimes, final RoutineType routineType, final String routineDescription) {
        return routineTimes.stream()
                .map(routineTime -> DailyRoutine.of(routineTime, routineType, routineDescription))
                .toList();
    }

    private List<Long> extractIds(final List<DailyRoutineEntity> savedDailyRoutines) {
        return savedDailyRoutines.stream()
                .map(DailyRoutineEntity::getId)
                .toList();
    }

    public List<DailyRoutineResponse> getDailyRoutine(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final MemberEntity member) {
        final List<DailyRoutineEntity> dailyRoutineEntities = dailyRoutineRepository.findAllByDateTimeBetween(startDateTime, endDateTime, member.getId());
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

    @Transactional
    public Long putDailyRoutine(final Long timeBlockId, final RoutineTime routineTime, final RoutineType routineType, final String description, final MemberEntity member) {
        final DailyRoutineEntity dailyRoutineEntity = getDailyRoutineEntity(timeBlockId, member);
        final DailyRoutine dailyRoutine = dailyRoutineEntity.toDomain();

        dailyRoutine.update(routineTime, routineType, description);
        dailyRoutineEntity.update(dailyRoutine);

        return timeBlockId;
    }

    private DailyRoutineEntity getDailyRoutineEntity(final Long timeBlockId, final MemberEntity member) {
        final DailyRoutineEntity dailyRoutineEntity = dailyRoutineRepository.findById(timeBlockId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 ID의 DailyRoutine이 존재하지 않습니다."));

        if (!dailyRoutineEntity.getMemberId().equals(member.getId())) {
            throw new BusinessException("본인의 DailyRoutine만 수정할 수 있습니다.");
        }

        return dailyRoutineEntity;
    }

    @Transactional
    public Long deleteDailyRoutine(final Long id) {
        dailyRoutineRepository.deleteById(id);
        return id;
    }
}
