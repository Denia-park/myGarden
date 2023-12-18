package org.hyunggi.mygardenbe.dailyroutine.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyRoutineControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("신규 Daily Routine을 등록한다.")
    void postDailyRoutine() throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime("2023-10-01T22:00:00")
                .endDateTime("2023-10-01T23:00:00")
                .routineType("STUDY")
                .routineDescription("자바 스터디")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/daily-routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Daily Routine 목록을 조회한다.")
    void getDailyRoutine() throws Exception {
        //given
        BDDMockito.given(dailyRoutineService.getDailyRoutine(any(), any()))
                .willReturn(
                        List.of(
                                DailyRoutineResponse.builder()
                                        .startDateTime("2023-10-01T22:00:00")
                                        .endDateTime("2023-10-01T23:00:00")
                                        .routineType("STUDY")
                                        .routineDescription("자바 스터디")
                                        .build()
                        )
                );

        //when, then
        mockMvc.perform(
                        get("/api/daily-routine")
                                .param("startDateTime", "2023-10-01T00:00:00")
                                .param("endDateTime", "2023-10-01T23:59:59")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }
}
