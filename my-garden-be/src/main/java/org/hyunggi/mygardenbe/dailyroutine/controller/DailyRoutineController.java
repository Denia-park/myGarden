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
@RequestMapping("/api/daily-routine")
@RequiredArgsConstructor
public class DailyRoutineController {
    private final DailyRoutineService dailyRoutineService;

    @GetMapping
    public ApiResponse<List<DailyRoutineResponse>> getDailyRoutine(@ModelAttribute @Valid final GetRequest getRequest, @LoginUserEntity MemberEntity member) {
        return ApiResponse.ok(dailyRoutineService.getDailyRoutine(getRequest.startDateTime(), getRequest.endDateTime(), member));
    }

    @PostMapping
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

    @PutMapping("/{timeBlockId}")
    public ApiResponse<Long> putDailyRoutine(@PathVariable final Long timeBlockId, @RequestBody @Valid final PostRequest request, @LoginUserEntity MemberEntity member) {
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.parse(request.startDateTime()),
                LocalDateTime.parse(request.endDateTime())
        );
        final RoutineType routineType = RoutineType.valueOf(request.routineType());

        final Long updatedId = dailyRoutineService.putDailyRoutine(timeBlockId, routineTime, routineType, request.routineDescription(), member);

        return ApiResponse.ok(updatedId);
    }

    @DeleteMapping("/{timeBlockId}")
    public ApiResponse<Long> deleteDailyRoutine(@PathVariable final Long timeBlockId, @LoginUserEntity MemberEntity member) {
        final Long deletedId = dailyRoutineService.deleteDailyRoutine(timeBlockId, member);

        return ApiResponse.ok(deletedId);
    }
}
