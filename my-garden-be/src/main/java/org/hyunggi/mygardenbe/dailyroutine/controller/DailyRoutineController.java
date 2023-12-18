package org.hyunggi.mygardenbe.dailyroutine.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.ApiResponse;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.GetRequest;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.TimeSplitter;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DailyRoutineController {
    private final DailyRoutineService dailyRoutineService;

    @PostMapping("/api/daily-routine")
    public ApiResponse<List<Long>> postDailyRoutine(@RequestBody @Valid final PostRequest request) {
        final List<RoutineTime> routineTimes = TimeSplitter.split(
                RoutineTime.of(
                        LocalDateTime.parse(request.startDateTime()),
                        LocalDateTime.parse(request.endDateTime())
                ));

        final List<Long> ids = dailyRoutineService.postDailyRoutine(routineTimes, request.routineType(), request.routineDescription());

        return ApiResponse.ok(ids);
    }

    @GetMapping("/api/daily-routine")
    public ApiResponse<List<DailyRoutineResponse>> getDailyRoutine(@ModelAttribute @Valid final GetRequest getRequest) {
        final List<DailyRoutineResponse> dailyRoutineResponses = dailyRoutineService.getDailyRoutine(getRequest.startDateTime(), getRequest.endDateTime());

        return ApiResponse.ok(dailyRoutineResponses);
    }
}
