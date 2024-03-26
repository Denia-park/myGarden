package org.hyunggi.mygardenbe.dailyroutine.service;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.common.exception.BusinessException;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineStudyHourResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class DailyRoutineServiceTest extends IntegrationTestSupport {
    @Autowired
    private DailyRoutineService dailyRoutineService;
    @Autowired
    private DailyRoutineRepository dailyRoutineRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberEntity member;

    @BeforeEach
    void memberSetUp() {
        Member memberDomain = new Member("test@test.com", "test1234!");

        member = memberRepository.save(MemberEntity.of(memberDomain, passwordEncoder));
    }

    @Test
    @DisplayName("dailyRoutines를 DB에 등록하고, id 목록을 반환한다.")
    void postDailyRoutine() {
        //given
        final RoutineTime routineTimeSample1 = getRoutineTimeSample1();
        final RoutineTime routineTimeSample2 = getRoutineTimeSample2();
        final List<RoutineTime> routineTimes = List.of(routineTimeSample1, routineTimeSample2);
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";

        //when
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);

        //then
        final List<DailyRoutineEntity> dailyRoutines = dailyRoutineRepository.findAll();
        assertThat(dailyRoutines).hasSize(2)
                .extracting("routineTime", "routineType", "routineDescription")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(routineTimeSample1, RoutineType.STUDY, "자바 스터디"),
                        Tuple.tuple(routineTimeSample2, RoutineType.STUDY, "자바 스터디")
                );
        assertThat(ids).hasSize(2)
                .doesNotContainNull();
    }

    private RoutineTime getRoutineTimeSample1() {
        return RoutineTime.of(
                LocalDateTime.of(2021, 10, 1, 22, 0, 0),
                LocalDateTime.of(2021, 10, 1, 23, 59, 59)
        );
    }

    private RoutineTime getRoutineTimeSample2() {
        return RoutineTime.of(
                LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 1, 0, 0)
        );
    }

    @Test
    @DisplayName("Daily Routine 목록을 조회한다.")
    void getDailyRoutine() {
        //given
        final RoutineTime routineTimeSample1 = getRoutineTimeSample1();
        final RoutineTime routineTimeSample2 = getRoutineTimeSample2();
        final List<RoutineTime> routineTimes = List.of(routineTimeSample1, routineTimeSample2);
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        postRoutine(routineTimes, routineType, routineDescription);

        //when
        final List<DailyRoutineResponse> dailyRoutineResponses = dailyRoutineService.getDailyRoutine(
                LocalDateTime.of(2021, 10, 1, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 23, 59, 59),
                member);

        //then
        assertThat(dailyRoutineResponses).hasSize(2)
                .extracting("startDateTime", "endDateTime", "routineType", "routineDescription")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("2021-10-01T22:00:00", "2021-10-01T23:59:59", "공부", "자바 스터디"),
                        Tuple.tuple("2021-10-02T00:00:00", "2021-10-02T01:00:00", "공부", "자바 스터디")
                );

        assertThat(dailyRoutineResponses)
                .extracting("id")
                .doesNotContainNull();
    }

    @Test
    @DisplayName("Daily Routine 목록을 조회할 때, 본인의 Daily Routine만 조회할 수 있다.")
    void getDailyRoutine_OnlyMine() {
        //given
        MemberEntity anotherMember = memberRepository.save(MemberEntity.of(new Member("anotherMember@test.com", "test1234!"), passwordEncoder));

        final RoutineTime routineTimeSample1 = getRoutineTimeSample1();
        final RoutineTime routineTimeSample2 = getRoutineTimeSample2();
        final List<RoutineTime> routineTimes = List.of(routineTimeSample1, routineTimeSample2);
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        postRoutine(routineTimes, routineType, routineDescription);

        //when
        final List<DailyRoutineResponse> dailyRoutineResponses = dailyRoutineService.getDailyRoutine(
                LocalDateTime.of(2021, 10, 1, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 23, 59, 59),
                anotherMember);

        //then
        assertThat(dailyRoutineResponses).isEmpty();
    }

    @Test
    @DisplayName("Daily Routine를 수정한다.")
    void putDailyRoutine() {
        //given
        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        final RoutineTime updateRoutineTime = RoutineTime.of(
                LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 3, 30, 0)
        );
        final RoutineType updateRoutineType = RoutineType.EXERCISE;
        final String updateRoutineDescription = "운동";

        //when
        final Long putId = dailyRoutineService.putDailyRoutine(postId, updateRoutineTime, updateRoutineType, updateRoutineDescription, member);

        //then
        final DailyRoutineEntity dailyRoutine = dailyRoutineRepository.findById(postId).get();

        assertThat(putId).isEqualTo(postId);
        assertThat(dailyRoutine).isNotNull();
        assertThat(dailyRoutine.getRoutineTime()).isEqualTo(updateRoutineTime);
        assertThat(dailyRoutine.getRoutineType()).isEqualTo(updateRoutineType);
        assertThat(dailyRoutine.getRoutineDescription()).isEqualTo(updateRoutineDescription);
    }


    @Test
    @DisplayName("Daily Routine을 수정할 때, 존재하지 않는 ID를 입력하면 예외가 발생한다.")
    void putDailyRoutine_IdIsNotExist() {
        //given
        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        final RoutineTime updateRoutineTime = RoutineTime.of(
                LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 3, 30, 0)
        );
        final RoutineType updateRoutineType = RoutineType.EXERCISE;
        final String updateRoutineDescription = "운동";

        //when, then
        assertThatThrownBy(() -> dailyRoutineService.putDailyRoutine(postId + 1, updateRoutineTime, updateRoutineType, updateRoutineDescription, member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당하는 ID의 DailyRoutine이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정한 Daily Routine의 날짜가 오늘 날짜가 아니면 예외가 발생한다.")
    void putDailyRoutine_UpdateDailyRoutineIsNotToday() {
        //given
        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        final RoutineTime updateRoutineTime = RoutineTime.of(
                LocalDateTime.of(2021, 10, 3, 0, 0, 0),
                LocalDateTime.of(2021, 10, 3, 3, 30, 0)
        );
        final RoutineType updateRoutineType = RoutineType.EXERCISE;
        final String updateRoutineDescription = "운동";

        //when, then
        assertThatThrownBy(() -> dailyRoutineService.putDailyRoutine(postId, updateRoutineTime, updateRoutineType, updateRoutineDescription, member))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("동일한 날짜의 시간으로만 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("본인의 Daily Routine만 수정할 수 있다.")
    void putDailyRoutine_UpdateDailyRoutineIsNotMine() {
        //given
        MemberEntity anotherMember = memberRepository.save(MemberEntity.of(new Member("another@test.com", "test1234!"), passwordEncoder));

        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        final RoutineTime updateRoutineTime = RoutineTime.of(
                LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 3, 30, 0)
        );
        final RoutineType updateRoutineType = RoutineType.EXERCISE;
        final String updateRoutineDescription = "운동";

        //when, then
        assertThatThrownBy(() -> dailyRoutineService.putDailyRoutine(postId, updateRoutineTime, updateRoutineType, updateRoutineDescription, anotherMember))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("본인의 DailyRoutine만 수정 및 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("Daily Routine를 삭제한다.")
    void deleteDailyRoutine() {
        //given
        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        //when
        dailyRoutineService.deleteDailyRoutine(postId, member);

        //then
        final Optional<DailyRoutineEntity> findDailyRoutineEntity = dailyRoutineRepository.findById(postId);
        assertThat(findDailyRoutineEntity).isEmpty();
    }

    @Test
    @DisplayName("Daily Routine를 삭제할 때, 존재하지 않는 ID를 입력하면 예외가 발생한다.")
    void deleteDailyRoutine_IdIsNotExist() {
        //given
        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        //when, then
        assertThatThrownBy(() -> dailyRoutineService.deleteDailyRoutine(postId + 1, member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당하는 ID의 DailyRoutine이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("Daily Routine를 삭제할 때, 본인의 Daily Routine만 삭제할 수 있다.")
    void deleteDailyRoutine_OnlyMine() {
        //given
        MemberEntity anotherMember = memberRepository.save(MemberEntity.of(new Member("another@test.com", "test1234!"), passwordEncoder));

        final List<RoutineTime> routineTimes = List.of(getRoutineTimeSample2());
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription, member);
        final Long postId = ids.get(0);

        //when, then
        assertThatThrownBy(() -> dailyRoutineService.deleteDailyRoutine(postId, anotherMember))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("본인의 DailyRoutine만 수정 및 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("(targetDate - 1년) ~ (targetDate - 1일)까지의 공부 시간 조회한다. (STUDY 타입만 조회, 오늘 공부 시간은 포함하지 않으며 1시간 단위로 계산)")
    void getStudyHours() {
        //given
        final LocalDate today = LocalDate.of(2024, 3, 3);

        final RoutineTime routineTimeSample1 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 1, 20, 0, 0),
                LocalDateTime.of(2024, 3, 1, 22, 0, 0)
        );
        final RoutineType routineTypeExercise = RoutineType.EXERCISE;
        final String routineDescriptionExercise = "운동";
        postRoutine(List.of(routineTimeSample1), routineTypeExercise, routineDescriptionExercise);

        final RoutineTime routineTimeSample2 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 1, 22, 0, 0),
                LocalDateTime.of(2024, 3, 1, 23, 59, 59)
        );
        final RoutineTime routineTimeSample3 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 2, 0, 0, 0),
                LocalDateTime.of(2024, 3, 2, 1, 13, 0)
        );
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        postRoutine(List.of(routineTimeSample2, routineTimeSample3), routineType, routineDescription);

        //when
        final List<DailyRoutineStudyHourResponse> studyHours = dailyRoutineService.getStudyHours(today, member);

        //then
        assertThat(studyHours).hasSize(2)
                .extracting("date", "studyHour")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("2024-03-01", 1),
                        Tuple.tuple("2024-03-02", 1)
                );
    }

    private void postRoutine(final List<RoutineTime> routineTimeSample3, final RoutineType routineTypeExercise, final String routineDescriptionExercise) {
        dailyRoutineService.postDailyRoutine(routineTimeSample3, routineTypeExercise, routineDescriptionExercise, member);
    }

    @Test
    @DisplayName("memberEmail을 통해서, 해당 member의 (targetDate - 1년) ~ (targetDate - 1일)까지의 공부 시간을 조회한다. (STUDY 타입만 조회, 오늘 공부 시간은 포함하지 않으며 1시간 단위로 계산)")
    void getStudyHoursWithoutLogin() {
        //given
        final String memberEmail = "test@test.com";
        final LocalDate tomorrow = LocalDate.of(2024, 3, 4);

        final RoutineTime routineTimeSample1 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 1, 20, 0, 0),
                LocalDateTime.of(2024, 3, 1, 22, 0, 0)
        );
        final RoutineType routineTypeExercise = RoutineType.EXERCISE;
        final String routineDescriptionExercise = "운동";
        postRoutine(List.of(routineTimeSample1), routineTypeExercise, routineDescriptionExercise);

        final RoutineTime routineTimeSample2 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 1, 22, 0, 0),
                LocalDateTime.of(2024, 3, 1, 23, 59, 59)
        );
        final RoutineTime routineTimeSample3 = RoutineTime.of(
                LocalDateTime.of(2024, 3, 2, 0, 0, 0),
                LocalDateTime.of(2024, 3, 2, 1, 13, 0)
        );
        final RoutineType routineType = RoutineType.STUDY;
        final String routineDescription = "자바 스터디";
        postRoutine(List.of(routineTimeSample2, routineTimeSample3), routineType, routineDescription);

        //when
        final List<DailyRoutineStudyHourResponse> studyHours = dailyRoutineService.getStudyHoursWithoutLogin(tomorrow, memberEmail);

        //then
        assertThat(studyHours).hasSize(2)
                .extracting("date", "studyHour")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("2024-03-01", 1),
                        Tuple.tuple("2024-03-02", 1)
                );
    }
}
