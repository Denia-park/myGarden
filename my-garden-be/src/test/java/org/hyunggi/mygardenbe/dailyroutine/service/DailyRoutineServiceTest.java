package org.hyunggi.mygardenbe.dailyroutine.service;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.ServiceTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.repository.DailyRoutineRepository;
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
    @DisplayName("dailyRoutine을 등록한다.")
    void postDailyRoutine() {
        //given
        final RoutineTime routineTimeSample1 = getRoutineTimeSample1();
        final RoutineTime routineTimeSample2 = getRoutineTimeSample2();
        final List<RoutineTime> routineTimes = List.of(routineTimeSample1, routineTimeSample2);
        final String routineType = "STUDY";
        final String routineDescription = "자바 스터디";

        //when
        dailyRoutineService.postDailyRoutine(routineTimes, routineType, routineDescription);

        //then
        final List<DailyRoutine> dailyRoutines = dailyRoutineRepository.findAll();
        assertThat(dailyRoutines).hasSize(2)
                .extracting("routineTime", "routineType", "routineDescription")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(routineTimeSample1, RoutineType.STUDY, "자바 스터디"),
                        Tuple.tuple(routineTimeSample2, RoutineType.STUDY, "자바 스터디")
                );
    }

    private RoutineTime getRoutineTimeSample2() {
        return RoutineTime.of(
                LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                LocalDateTime.of(2021, 10, 2, 1, 0, 0)
        );
    }

    private RoutineTime getRoutineTimeSample1() {
        return RoutineTime.of(
                LocalDateTime.of(2021, 10, 1, 22, 0, 0),
                LocalDateTime.of(2021, 10, 1, 23, 59, 59)
        );
    }
}
