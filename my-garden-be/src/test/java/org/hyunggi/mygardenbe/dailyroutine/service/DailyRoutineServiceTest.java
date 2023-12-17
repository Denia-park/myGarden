package org.hyunggi.mygardenbe.dailyroutine.service;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.ServiceTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DailyRoutineServiceTest extends ServiceTestSupport {
    @Autowired
    private DailyRoutineService dailyRoutineService;
    @Autowired
    private DailyRoutineRepository dailyRoutineRepository;

    @Test
    @DisplayName("dailyRoutines를 DB에 등록하고, id 목록을 반환한다.")
    void postDailyRoutine() {
        //given
        final RoutineTime routineTimeSample1 = getRoutineTimeSample1();
        final RoutineTime routineTimeSample2 = getRoutineTimeSample2();
        final List<RoutineTime> routineTimes = List.of(routineTimeSample1, routineTimeSample2);
        final String routineType = "STUDY";
        final String routineDescription = "자바 스터디";

        //when
        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription);

        //then
        final List<DailyRoutineEntity> dailyRoutines = dailyRoutineRepository.findAll();
        assertThat(dailyRoutines).hasSize(2)
                .extracting("routineTime", "routineType", "routineDescription")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(routineTimeSample1, RoutineType.STUDY, "자바 스터디"),
                        Tuple.tuple(routineTimeSample2, RoutineType.STUDY, "자바 스터디")
                );
        assertThat(ids).hasSize(2);
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
        final String routineType = "STUDY";
        final String routineDescription = "자바 스터디";
        dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription);

        //when
        final List<DailyRoutineResponse> dailyRoutineResponses = dailyRoutineService.getDailyRoutine(
                LocalDateTime.of(2021, 10, 1, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 23, 59, 59)
        );

        //then
        assertThat(dailyRoutineResponses).hasSize(2)
                .extracting("startDateTime", "endDateTime", "routineType", "routineDescription")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("2021-10-01T22:00:00", "2021-10-01T23:59:59", "공부", "자바 스터디"),
                        Tuple.tuple("2021-10-02T00:00:00", "2021-10-02T01:00:00", "공부", "자바 스터디")
                );
    }
}
