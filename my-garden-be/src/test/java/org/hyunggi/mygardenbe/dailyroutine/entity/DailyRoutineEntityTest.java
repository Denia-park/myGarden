package org.hyunggi.mygardenbe.dailyroutine.entity;

import org.assertj.core.groups.Tuple;
import org.hyunggi.mygardenbe.dailyroutine.domain.DailyRoutine;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DailyRoutineEntityTest {

    @Test
    @DisplayName("dailyRoutine을 이용해서 dailyRoutineEntity를 생성할 수 있다.")
    void of() {
        //given
        final Long memberId = 1L;
        final DailyRoutine dailyRoutine = getDailyRoutine(
                getRoutineTime(9, 10),
                "SLEEP",
                "낮잠"
        );

        //when
        final DailyRoutineEntity dailyRoutineEntity = DailyRoutineEntity.of(dailyRoutine, memberId);

        //then
        assertThat(dailyRoutineEntity).isNotNull();
        assertThat(dailyRoutineEntity.getRoutineTime()).isEqualTo(getRoutineTime(9, 10));
        assertThat(dailyRoutineEntity.getRoutineType()).isEqualTo(RoutineType.SLEEP);
        assertThat(dailyRoutineEntity.getRoutineDescription()).isEqualTo("낮잠");
        assertThat(dailyRoutineEntity.getMemberId()).isEqualTo(memberId);
    }

    private RoutineTime getRoutineTime(final int startHour, final int endHour) {
        return RoutineTime.of(
                LocalDateTime.of(2023, 12, 14, startHour, 0, 0),
                LocalDateTime.of(2023, 12, 14, endHour, 0, 0)
        );
    }

    private DailyRoutine getDailyRoutine(final RoutineTime routineTime, final String routineType, final String routineDescription) {
        return DailyRoutine.of(
                routineTime,
                routineType,
                routineDescription
        );
    }

    @Test
    @DisplayName("dailyRoutine List를 이용해서 dailyRoutineEntity를 생성할 수 있다.")
    void of_listOfDailyRoutine() {
        //given
        final Long memberId = 1L;

        final DailyRoutine dailyRoutine1 = getDailyRoutine(
                getRoutineTime(9, 10),
                "SLEEP",
                "낮잠"
        );
        final DailyRoutine dailyRoutine2 = getDailyRoutine(
                getRoutineTime(10, 11),
                "STUDY",
                "공부"
        );

        List<DailyRoutine> dailyRoutines = List.of(dailyRoutine1, dailyRoutine2);

        //when
        final List<DailyRoutineEntity> dailyRoutineEntities = DailyRoutineEntity.of(dailyRoutines, memberId);

        //then
        assertThat(dailyRoutineEntities).isNotNull()
                .hasSize(2)
                .extracting("routineTime", "routineType", "routineDescription", "memberId")
                .containsExactly(
                        Tuple.tuple(
                                getRoutineTime(9, 10),
                                RoutineType.SLEEP,
                                "낮잠",
                                memberId
                        ),
                        Tuple.tuple(
                                getRoutineTime(10, 11),
                                RoutineType.STUDY,
                                "공부",
                                memberId
                        )
                );
    }

    @Test
    @DisplayName("dailyRoutineEntity를 이용해서 dailyRoutine을 생성할 수 있다.")
    void toDomain() {
        //given
        final Long memberId = 1L;

        final DailyRoutine savedDailyRoutine = getDailyRoutine(
                getRoutineTime(9, 10),
                "SLEEP",
                "낮잠"
        );
        final DailyRoutineEntity dailyRoutineEntity = DailyRoutineEntity.of(savedDailyRoutine, memberId);

        //when
        final DailyRoutine dailyRoutine = dailyRoutineEntity.toDomain();

        //then
        assertThat(dailyRoutine).isNotNull();
        assertThat(dailyRoutine.getRoutineTime()).isEqualTo(getRoutineTime(9, 10));
        assertThat(dailyRoutine.getRoutineType()).isEqualTo(RoutineType.SLEEP);
        assertThat(dailyRoutine.getRoutineDescription()).isEqualTo("낮잠");
    }

    @Test
    @DisplayName("dailyRoutine를 이용해서 dailyRoutineEntity를 수정할 수 있다.")
    void update() {
        //given
        final Long memberId = 1L;

        final DailyRoutine savedDailyRoutine = getDailyRoutine(
                getRoutineTime(9, 10),
                "SLEEP",
                "낮잠"
        );
        final DailyRoutineEntity dailyRoutineEntity = DailyRoutineEntity.of(savedDailyRoutine, memberId);

        //when
        final DailyRoutine newDailyRoutine = getDailyRoutine(
                getRoutineTime(10, 11),
                "STUDY",
                "공부"
        );
        dailyRoutineEntity.update(newDailyRoutine);

        //then
        assertThat(dailyRoutineEntity).isNotNull();
        assertThat(dailyRoutineEntity.getRoutineTime()).isEqualTo(newDailyRoutine.getRoutineTime());
        assertThat(dailyRoutineEntity.getRoutineType()).isEqualTo(newDailyRoutine.getRoutineType());
        assertThat(dailyRoutineEntity.getRoutineDescription()).isEqualTo(newDailyRoutine.getRoutineDescription());
        assertThat(dailyRoutineEntity.getMemberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("dailyRoutineEntity의 멤버 아이디와 비교하여 본인의 DailyRoutine인지 확인할 수 있다.")
    void isNotOwner() {
        //given
        final Long memberId = 1L;
        final DailyRoutine dailyRoutine = getDailyRoutine(
                getRoutineTime(9, 10),
                "SLEEP",
                "낮잠"
        );
        final DailyRoutineEntity dailyRoutineEntity = DailyRoutineEntity.of(dailyRoutine, memberId);

        //when
        final boolean isNotOwner = dailyRoutineEntity.isNotOwner(2L);

        //then
        assertThat(isNotOwner).isTrue();
    }
}
