package org.hyunggi.mygardenbe.dailyroutine.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PutRequest;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @ParameterizedTest
    @CsvSource({
            ", 2023-10-01T23:00:00",
            "2023-10-01T22:00:00, "
    })
    @DisplayName("Daily Routine을 등록할 때, 시작시간 혹은 종료시간을 입력하지 않으면 요청에 실패한다.")
    void throwExceptionWhenStartTimeOrEndTimeIsNull(final String startDateTime, final String endDateTime) throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .routineType("STUDY")
                .routineDescription("자바 스터디")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/daily-routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Daily Routine을 등록할 때, 루틴 타입이 없으면 요청에 실패한다.")
    void throwExceptionWhenRoutineTypeIsEmpty() throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime("2023-10-01T21:00:00")
                .endDateTime("2023-10-01T22:00:00")
                .routineType("")
                .routineDescription("자바 스터디")
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/daily-routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Daily Routine을 등록할 때, 루틴 설명이 없으면 요청에 실패한다.")
    void throwExceptionWhenRoutineDescriptionIsNull() throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime("2023-10-01T21:00:00")
                .endDateTime("2023-10-01T22:00:00")
                .routineType("STUDY")
                .routineDescription(null)
                .build();

        //when, then
        mockMvc.perform(
                        post("/api/daily-routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Daily Routine 목록을 조회한다.")
    void getDailyRoutine() throws Exception {
        //given
        BDDMockito.given(dailyRoutineService.getDailyRoutine(any(), any()))
                .willReturn(
                        List.of(
                                DailyRoutineResponse.builder()
                                        .id(1L)
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

    @ParameterizedTest
    @CsvSource({
            ", 2023-10-01T23:00:00",
            "2023-10-01T22:00:00, "
    })
    @DisplayName("Daily Routine 목록을 조회할 때, 시작시간 혹은 종료시간을 입력하지 않으면 요청에 실패한다.")
    void throwExceptionWhenStartDateTimeOrEndDateTimeIsNull(final String startDateTime, final String endDateTime) throws Exception {
        //given, when, then
        mockMvc.perform(
                        get("/api/daily-routine")
                                .param("startDateTime", startDateTime)
                                .param("endDateTime", endDateTime)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Daily Routine을 수정한다.")
    void putDailyRoutine() throws Exception {
        //given
        final PutRequest request = PutRequest.builder()
                .id(1L)
                .startDateTime("2023-10-01T22:00:00")
                .endDateTime("2023-10-01T23:00:00")
                .routineType("STUDY")
                .routineDescription("자바 스터디")
                .build();
        BDDMockito.given(dailyRoutineService.putDailyRoutine(any(), any(), any(), any()))
                .willReturn(1L);

        //when, then
        mockMvc.perform(
                        put("/api/daily-routine/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1L));
    }
}
