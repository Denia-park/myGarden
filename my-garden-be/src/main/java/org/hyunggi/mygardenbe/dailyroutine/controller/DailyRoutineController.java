package org.hyunggi.mygardenbe.dailyroutine.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.auth.LoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.GetRequest;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.domain.TimeSplitter;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DailyRoutineController {
    private final DailyRoutineService dailyRoutineService;

    @GetMapping("/api/daily-routine")
    public ApiResponse<List<DailyRoutineResponse>> getDailyRoutine(@ModelAttribute @Valid final GetRequest getRequest, @LoginUserEntity MemberEntity member) {
        final List<DailyRoutineResponse> dailyRoutineResponses = dailyRoutineService.getDailyRoutine(getRequest.startDateTime(), getRequest.endDateTime(), member);

        return ApiResponse.ok(dailyRoutineResponses);
    }

    @PostMapping("/api/daily-routine")
    public ApiResponse<List<Long>> postDailyRoutine(@RequestBody @Valid final PostRequest request, @LoginUserEntity MemberEntity member) {
        final List<RoutineTime> routineTimes = TimeSplitter.split(
                RoutineTime.of(
                        LocalDateTime.parse(request.startDateTime()),
                        LocalDateTime.parse(request.endDateTime())
                )
        );
        final RoutineType routineType = RoutineType.valueOf(request.routineType());

        final List<Long> dailyRoutineIds = dailyRoutineService.postDailyRoutine(routineTimes, routineType, request.routineDescription(), member);

        return ApiResponse.ok(dailyRoutineIds);
    }

    @PutMapping("/api/daily-routine/{id}")
    public ApiResponse<Long> putDailyRoutine(@PathVariable final Long id, @RequestBody @Valid final PostRequest request) {
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.parse(request.startDateTime()),
                LocalDateTime.parse(request.endDateTime())
        );
        final RoutineType routineType = RoutineType.valueOf(request.routineType());

        final Long updatedId = dailyRoutineService.putDailyRoutine(id, routineTime, routineType, request.routineDescription());

        return ApiResponse.ok(updatedId);
    }

    @DeleteMapping("/api/daily-routine/{id}")
    public ApiResponse<Long> deleteDailyRoutine(@PathVariable final Long id) {
        final Long deletedId = dailyRoutineService.deleteDailyRoutine(id);

        return ApiResponse.ok(deletedId);
    }
}
