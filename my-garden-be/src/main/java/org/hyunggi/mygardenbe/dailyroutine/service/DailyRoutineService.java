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
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineStudyHourResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 데일리 루틴 Service
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyRoutineService {
    /**
     * 데일리 루틴 Entity Repository
     */
    private final DailyRoutineRepository dailyRoutineRepository;

    /**
     * 유저 Entity Repository
     */
    private final MemberRepository memberRepository;

    /**
     * 데일리 루틴 등록
     *
     * @param routineTimes       루틴 시간 목록
     * @param routineType        루틴 타입
     * @param routineDescription 루틴 설명
     * @param member             유저 Entity
     * @return 등록한 데일리 루틴 ID 목록
     */
    @Transactional
    public List<Long> postDailyRoutine(final List<RoutineTime> routineTimes, final RoutineType routineType, final String routineDescription, final MemberEntity member) {
        final List<DailyRoutine> dailyRoutines = convertDailyRoutines(routineTimes, routineType, routineDescription);

        return extractIds(dailyRoutineRepository.saveAll(DailyRoutineEntity.of(dailyRoutines, member.getId())));
    }

    /**
     * 루틴 시간 목록을 DailyRoutine 목록으로 변환
     *
     * @param routineTimes       루틴 시간 목록
     * @param routineType        루틴 타입
     * @param routineDescription 루틴 설명
     * @return DailyRoutine 목록
     */
    private List<DailyRoutine> convertDailyRoutines(final List<RoutineTime> routineTimes, final RoutineType routineType, final String routineDescription) {
        return routineTimes.stream()
                .map(routineTime -> DailyRoutine.of(routineTime, routineType, routineDescription))
                .toList();
    }

    /**
     * DailyRoutine 목록에서 ID 목록 추출
     *
     * @param savedDailyRoutines 저장된 DailyRoutine 목록
     * @return DailyRoutine ID 목록
     */
    private List<Long> extractIds(final List<DailyRoutineEntity> savedDailyRoutines) {
        return savedDailyRoutines.stream()
                .map(DailyRoutineEntity::getId)
                .toList();
    }

    /**
     * 데일리 루틴 조회
     *
     * @param startDateTime 조회 시작 시간
     * @param endDateTime   조회 종료 시간
     * @param member        유저 Entity
     * @return 데일리 루틴 목록
     */
    public List<DailyRoutineResponse> getDailyRoutine(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final MemberEntity member) {
        final List<DailyRoutineEntity> dailyRoutineEntities = dailyRoutineRepository.findAllByDateTimeBetween(startDateTime, endDateTime, member.getId());
        final Map<Long, DailyRoutine> dailyRoutines = convertMapWithKeyAndDailyRoutine(dailyRoutineEntities);

        return convertDailyRoutineResponses(dailyRoutines);
    }

    /**
     * DailyRoutineEntity 목록을 Key와 DailyRoutine의 Map으로 변환
     *
     * @param dailyRoutineEntities DailyRoutineEntity 목록
     * @return Key와 DailyRoutine의 Map
     */
    private Map<Long, DailyRoutine> convertMapWithKeyAndDailyRoutine(final List<DailyRoutineEntity> dailyRoutineEntities) {
        return dailyRoutineEntities.stream()
                .collect(Collectors.toMap(DailyRoutineEntity::getId, DailyRoutineEntity::toDomain));
    }

    /**
     * Key와 DailyRoutine의 Map을 DailyRoutineResponse 목록으로 변환
     *
     * @param dailyRoutines Key와 DailyRoutine의 Map
     * @return DailyRoutineResponse 목록
     */
    private List<DailyRoutineResponse> convertDailyRoutineResponses(final Map<Long, DailyRoutine> dailyRoutines) {
        return dailyRoutines.entrySet().stream()
                .map(entry -> DailyRoutineResponse.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyRoutineResponse::startDateTime))
                .toList();
    }

    /**
     * 데일리 루틴 수정
     *
     * @param timeBlockId 수정할 데일리 루틴 ID
     * @param routineTime 루틴 시간
     * @param routineType 루틴 타입
     * @param description 루틴 설명
     * @param member      유저 Entity
     * @return 수정한 데일리 루틴 ID
     */
    @Transactional
    public Long putDailyRoutine(final Long timeBlockId, final RoutineTime routineTime, final RoutineType routineType, final String description, final MemberEntity member) {
        final DailyRoutineEntity dailyRoutineEntity = getDailyRoutineEntity(timeBlockId, member);
        final DailyRoutine dailyRoutine = dailyRoutineEntity.toDomain();

        dailyRoutine.update(routineTime, routineType, description);
        dailyRoutineEntity.update(dailyRoutine);

        return timeBlockId;
    }

    /**
     * timeBlockId를 사용하여 DailyRoutineEntity 조회하고, 본인의 DailyRoutine인지 확인
     *
     * @param timeBlockId 수정할 데일리 루틴 ID
     * @param member      유저 Entity
     * @return DailyRoutineEntity
     */
    private DailyRoutineEntity getDailyRoutineEntity(final Long timeBlockId, final MemberEntity member) {
        final DailyRoutineEntity dailyRoutineEntity = dailyRoutineRepository.findById(timeBlockId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 ID의 DailyRoutine이 존재하지 않습니다."));

        if (dailyRoutineEntity.isNotOwner(member.getId())) {
            throw new BusinessException("본인의 DailyRoutine만 수정 및 삭제할 수 있습니다.");
        }

        return dailyRoutineEntity;
    }

    /**
     * 데일리 루틴 삭제
     *
     * @param timeBlockId 삭제할 데일리 루틴 ID
     * @param member      유저 Entity
     * @return 삭제한 데일리 루틴 ID
     */
    @Transactional
    public Long deleteDailyRoutine(final Long timeBlockId, final MemberEntity member) {
        final DailyRoutineEntity dailyRoutineEntity = getDailyRoutineEntity(timeBlockId, member);

        dailyRoutineRepository.deleteById(dailyRoutineEntity.getId());
        return timeBlockId;
    }

    /**
     * (targetDate - 1년) ~ (targetDate - 1일)까지의 공부 시간을 조회
     * <br/><br/>
     * targetDate의 공부 시간은 미포함 -> targetDate에 해당하는 공부시간은 클라이언트단에서 계산
     *
     * @param targetDate 오늘 날짜
     * @param member     유저 Entity
     * @return 공부 시간 목록
     */
    public List<DailyRoutineStudyHourResponse> getStudyHours(final LocalDate targetDate, final MemberEntity member) {
        final LocalDateTime startDateTime = targetDate.atStartOfDay().minusYears(1);
        final LocalDateTime endDateTime = targetDate.atTime(LocalTime.MAX).minusDays(1);

        return getDailyRoutine(startDateTime, endDateTime, member).stream()
                .filter(routine -> routine.isEqualType(RoutineType.STUDY))
                .collect(Collectors.groupingBy(DailyRoutineResponse::getStartDate, Collectors.summingInt(DailyRoutineResponse::calculateMinutesBetweenStartAndEnd)))
                .entrySet().stream()
                .map(entry -> new DailyRoutineStudyHourResponse(entry.getKey(), convertMinToHour(entry.getValue())))
                .toList();
    }

    /**
     * 분을 시간으로 변환
     *
     * @param min 분
     * @return 시간
     */
    private int convertMinToHour(final int min) {
        return min / 60;
    }

    /**
     * 로그인 없이 memberEmail을 통해 해당 member의 (targetDate - 1년) ~ (targetDate - 1일)까지의 공부 시간을 조회한다.
     *
     * @param targetDate  조회 날짜
     * @param memberEmail 조회할 사용자 이메일
     * @return 공부 시간 목록
     */
    public List<DailyRoutineStudyHourResponse> getStudyHoursWithoutLogin(final LocalDate targetDate, final String memberEmail) {
        return getStudyHours(targetDate, findMemberByEmail(memberEmail));
    }

    /**
     * memberEmail을 통해 MemberEntity 조회
     *
     * @param memberEmail 조회할 사용자 이메일
     * @return MemberEntity
     */
    private MemberEntity findMemberByEmail(final String memberEmail) {
        return memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 이메일의 사용자가 존재하지 않습니다."));
    }
}
