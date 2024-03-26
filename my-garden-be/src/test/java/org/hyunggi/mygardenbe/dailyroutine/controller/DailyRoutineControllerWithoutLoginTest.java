package org.hyunggi.mygardenbe.dailyroutine.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineStudyHourResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.BDDMockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyRoutineControllerWithoutLoginTest extends ControllerTestSupport {
    @Test
    @DisplayName("로그인 하지 않은 사용자가 memberEmail을 통해서, 해당 member의 공부 시간 목록을 조회한다.")
    void getStudyHoursWithoutLogin() throws Exception {
        //given
        final String urlSafeBase64MemberEmail = "dGVzdEB0ZXN0LmNvbQ"; //test@test.com
        BDDMockito.given(dailyRoutineService.getStudyHoursWithoutLogin(any(), any()))
                .willReturn(
                        List.of(
                                new DailyRoutineStudyHourResponse("2024-03-21", 3),
                                new DailyRoutineStudyHourResponse("2024-03-22", 4)
                        )
                );

        //when, then
        mockMvc.perform(
                        get("/api/daily-routine/study-hours/without-login")
                                .param("memberEmail", urlSafeBase64MemberEmail)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].date").value("2024-03-21"))
                .andExpect(jsonPath("$.data[0].count").value(3))
                .andExpect(jsonPath("$.data[1].date").value("2024-03-22"))
                .andExpect(jsonPath("$.data[1].count").value(4));
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("로그인 하지 않은 사용자가 memberEmail을 빈 문자열로 전달하면, 요청에 실패한다.")
    void throwExceptionWhenMemberEmailIsNullOrEmpty(final String memberEmail) throws Exception {
        //when, then
        mockMvc.perform(
                        get("/api/daily-routine/study-hours/without-login")
                                .param("memberEmail", memberEmail)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("memberEmail은 필수 값입니다."));
    }

    @Test
    @DisplayName("로그인 하지 않은 사용자가 memberEmail을 전달하지 않으면, 요청에 실패한다.")
    void throwExceptionWhenMemberEmailIsNull() throws Exception {
        //when, then
        mockMvc.perform(
                        get("/api/daily-routine/study-hours/without-login")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'memberEmail' for method parameter type String is not present"));
    }
}
