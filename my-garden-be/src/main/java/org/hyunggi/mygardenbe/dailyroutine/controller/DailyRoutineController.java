package org.hyunggi.mygardenbe.dailyroutine.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.ApiResponse;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.TimeSplitter;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DailyRoutineController {
    private final DailyRoutineService dailyRoutineService;

    @PostMapping("/api/daily-routine")
    public ApiResponse<List<Long>> postDailyRoutine(@RequestBody @Valid final Request request) {
        final List<RoutineTime> routineTimes = TimeSplitter.split(
                RoutineTime.of(
                        LocalDateTime.parse(request.startDateTime),
                        LocalDateTime.parse(request.endDateTime)
                ));

        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, request.routineType, request.routineDescription);

        return ApiResponse.ok(ids);
    }

    @Builder
    public record Request(
            @NotBlank(message = "데일리 루틴의 시작 시간은 비어있을 수 없습니다.")
            String startDateTime,
            @NotBlank(message = "데일리 루틴의 종료 시간은 비어있을 수 없습니다.")
            String endDateTime,
            @NotBlank(message = "데일리 루틴의 타입은 비어있을 수 없습니다.")
            String routineType,
            @NotNull(message = "데일리 루틴의 설명은 null이 될 수 없습니다.")
            String routineDescription
    ) {
    }
}
