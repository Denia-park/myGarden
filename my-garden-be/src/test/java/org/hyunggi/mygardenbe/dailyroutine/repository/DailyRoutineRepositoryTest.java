package org.hyunggi.mygardenbe.dailyroutine.repository;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.entity.DailyRoutineEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class DailyRoutineRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private DailyRoutineRepository dailyRoutineRepository;

    @Test
    @DisplayName("원하는 기간의 DailyRoutine만을 조회한다.")
    void findAllByDateTimeBetween() {
        //given
        final List<DailyRoutine> dailyRoutines = List.of(
                createDailyRoutine(12, 16),
                createDailyRoutine(12, 17)
        );

        dailyRoutineRepository.saveAll(DailyRoutineEntity.of(dailyRoutines));

        //when
        final List<DailyRoutineEntity> foundDailyRoutines = dailyRoutineRepository.findAllByDateTimeBetween(
                LocalDateTime.of(2023, 12, 17, 0, 0, 0),
                LocalDateTime.of(2023, 12, 17, 23, 59, 59)
        );

        //then
        assertThat(foundDailyRoutines).hasSize(1)
                .extracting("routineTime.startDateTime", "routineTime.endDateTime", "routineType", "routineDescription")
                .containsExactly(
                        Tuple.tuple(
                                LocalDateTime.of(2023, 12, 17, 22, 0, 0),
                                LocalDateTime.of(2023, 12, 17, 23, 59, 59),
                                RoutineType.STUDY,
                                "자바 스터디"
                        )
                );
    }

    private DailyRoutine createDailyRoutine(final int month, final int dayOfMonth) {
        return DailyRoutine.of(
                RoutineTime.of(
                        LocalDateTime.of(2023, month, dayOfMonth, 22, 0, 0),
                        LocalDateTime.of(2023, month, dayOfMonth, 23, 59, 59)
                ),
                "STUDY",
                "자바 스터디"
        );
    }
}
