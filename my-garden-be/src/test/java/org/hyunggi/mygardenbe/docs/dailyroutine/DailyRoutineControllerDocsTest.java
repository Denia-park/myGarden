package org.hyunggi.mygardenbe.docs.dailyroutine;

import org.hyunggi.mygardenbe.dailyroutine.controller.DailyRoutineController;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyRoutineControllerDocsTest extends RestDocsSupport {
    private final DailyRoutineService dailyRoutineService = Mockito.mock(DailyRoutineService.class);

    @Override
    protected Object initController() {
        return new DailyRoutineController(dailyRoutineService);
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

        final String startDateTime = "2023-10-01T00:00:00";
        final String endDateTime = "2023-10-01T23:59:59";

        //when, then
        mockMvc.perform(
                        get("/api/daily-routine")
                                .param("startDateTime", startDateTime)
                                .param("endDateTime", endDateTime)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("daily-routine/get-daily-routine"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , queryParameters(
                                parameterWithName("startDateTime").description("조회 시작일시 (yyyy-MM-ddTHH:mm:ss)"),
                                parameterWithName("endDateTime").description("조회 종료일시 (yyyy-MM-ddTHH:mm:ss)")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터 (TimeBlock 목록)"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("data[].startDateTime").type(JsonFieldType.STRING).description("시작일시"),
                                fieldWithPath("data[].endDateTime").type(JsonFieldType.STRING).description("종료일시"),
                                fieldWithPath("data[].routineType").type(JsonFieldType.STRING).description("루틴 타입"),
                                fieldWithPath("data[].routineDescription").type(JsonFieldType.STRING).description("루틴 설명")
                        )
                ));
    }
}
