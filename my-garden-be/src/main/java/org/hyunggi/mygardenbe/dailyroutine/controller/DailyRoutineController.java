package org.hyunggi.mygardenbe.dailyroutine.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.common.auth.annotation.WithLoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.GetRequest;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineTime;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.domain.TimeSplitter;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineStudyHourResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * 데일리 루틴 Controller
 */
@RestController
@RequestMapping("/api/daily-routine")
@RequiredArgsConstructor
public class DailyRoutineController {
    /**
     * 데일리 루틴 Service
     */
    private final DailyRoutineService dailyRoutineService;

    /**
     * 데일리 루틴 조회
     *
     * @param getRequest 조회 요청
     * @param member     유저 Entity
     * @return 데일리 루틴 목록 응답
     */
    @GetMapping
    public ApiResponse<List<DailyRoutineResponse>> getDailyRoutine(@ModelAttribute @Valid final GetRequest getRequest, @WithLoginUserEntity MemberEntity member) {
        return ApiResponse.ok(dailyRoutineService.getDailyRoutine(getRequest.startDateTime(), getRequest.endDateTime(), member));
    }

    /**
     * 데일리 루틴 등록
     *
     * @param request 데일리 루틴 등록 요청
     * @param member  유저 Entity
     * @return 등록한 데일리 루틴 ID 목록 응답
     */
    @PostMapping
    public ApiResponse<List<Long>> postDailyRoutine(@RequestBody @Valid final PostRequest request, @WithLoginUserEntity MemberEntity member) {
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

    /**
     * 데일리 루틴 수정
     *
     * @param timeBlockId 수정할 데일리 루틴 ID
     * @param request     데일리 루틴 수정 요청
     * @param member      유저 Entity
     * @return 수정한 데일리 루틴 ID 응답
     */
    @PutMapping("/{timeBlockId}")
    public ApiResponse<Long> putDailyRoutine(@PathVariable final Long timeBlockId, @RequestBody @Valid final PostRequest request, @WithLoginUserEntity MemberEntity member) {
        final RoutineTime routineTime = RoutineTime.of(
                LocalDateTime.parse(request.startDateTime()),
                LocalDateTime.parse(request.endDateTime())
        );
        final RoutineType routineType = RoutineType.valueOf(request.routineType());

        final Long updatedId = dailyRoutineService.putDailyRoutine(timeBlockId, routineTime, routineType, request.routineDescription(), member);

        return ApiResponse.ok(updatedId);
    }

    /**
     * 데일리 루틴 삭제
     *
     * @param timeBlockId 삭제할 데일리 루틴 ID
     * @param member      유저 Entity
     * @return 삭제한 데일리 루틴 ID 응답
     */
    @DeleteMapping("/{timeBlockId}")
    public ApiResponse<Long> deleteDailyRoutine(@PathVariable final Long timeBlockId, @WithLoginUserEntity MemberEntity member) {
        final Long deletedId = dailyRoutineService.deleteDailyRoutine(timeBlockId, member);

        return ApiResponse.ok(deletedId);
    }

    /**
     * 공부시간 조회 (로그인한 사용자)
     *
     * @param member 로그인한 사용자
     * @return 공부시간 목록 응답
     */
    @GetMapping("/study-hours")
    public ApiResponse<List<DailyRoutineStudyHourResponse>> getStudyHours(@WithLoginUserEntity MemberEntity member) {
        return ApiResponse.ok(dailyRoutineService.getStudyHours(LocalDate.now(), member));
    }

    /**
     * 공부시간 조회 (로그인하지 않은 사용자)
     * <br/><br/>
     * - 조회할 사용자 이메일을 URL Safe Base64로 인코딩하여 전달받는다.
     * - 현재까지의 공부시간을 조회하기 위해서, 내일 날짜를 넣어서 조회한다.
     *
     * @param urlSafeBase64MemberEmail 조회할 사용자 이메일
     * @return 공부시간 목록 응답
     */
    @GetMapping("/study-hours/without-login")
    public ApiResponse<List<DailyRoutineStudyHourResponse>> getStudyHoursWithoutLogin(@RequestParam("memberEmail") String urlSafeBase64MemberEmail) {
        final String memberEmail = convertUrlSafeBase64MemberEmailToMemberEmail(urlSafeBase64MemberEmail);
        final LocalDate tomorrow = LocalDate.now().plusDays(1);

        return ApiResponse.ok(dailyRoutineService.getStudyHoursWithoutLogin(tomorrow, memberEmail));
    }

    /**
     * URL Safe Base64로 인코딩된 사용자 이메일을 사용자 이메일로 변환
     *
     * @param urlSafeBase64MemberEmail URL Safe Base64로 인코딩된 사용자 이메일
     * @return 사용자 이메일
     */
    private String convertUrlSafeBase64MemberEmailToMemberEmail(final String urlSafeBase64MemberEmail) {
        if (urlSafeBase64MemberEmail == null || urlSafeBase64MemberEmail.isEmpty()) {
            throw new IllegalArgumentException("memberEmail is required");
        }

        return new String(Base64.getUrlDecoder().decode(urlSafeBase64MemberEmail));
    }
}
