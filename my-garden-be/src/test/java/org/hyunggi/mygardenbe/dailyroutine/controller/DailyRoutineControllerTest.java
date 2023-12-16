package org.hyunggi.mygardenbe.dailyroutine.controller;

import org.hyunggi.mygardenbe.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyRoutineControllerTest extends ControllerTestSupport {
    @Test
    @DisplayName("신규 Daily Routine을 등록한다.")
    void postDailyRoutine() throws Exception {
        //given
        final DailyRoutineController.Request request = DailyRoutineController.Request.builder()
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
}
