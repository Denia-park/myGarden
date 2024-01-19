package org.hyunggi.mygardenbe.docs.dailyroutine;

import org.hyunggi.mygardenbe.dailyroutine.controller.DailyRoutineController;
import org.hyunggi.mygardenbe.dailyroutine.controller.request.PostRequest;
import org.hyunggi.mygardenbe.dailyroutine.domain.RoutineType;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.dailyroutine.service.response.DailyRoutineResponse;
import org.hyunggi.mygardenbe.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.hyunggi.mygardenbe.docs.util.RestDocsUtil.allEnumString;
import static org.hyunggi.mygardenbe.docs.util.RestDocsUtil.field;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        BDDMockito.given(dailyRoutineService.getDailyRoutine(any(), any(), any()))
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
                                parameterWithName("startDateTime").description("조회 시작일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss")),
                                parameterWithName("endDateTime").description("조회 종료일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss"))
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터 (TimeBlock 목록)"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("data[].startDateTime").type(JsonFieldType.STRING).description("시작일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss")),
                                fieldWithPath("data[].endDateTime").type(JsonFieldType.STRING).description("종료일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss")),
                                fieldWithPath("data[].routineType").type(JsonFieldType.STRING).description("루틴 타입")
                                        .attributes(field("constraints", allEnumString(RoutineType.class))),
                                fieldWithPath("data[].routineDescription").type(JsonFieldType.STRING).description("루틴 설명")
                        )
                ));
    }

    @Test
    @DisplayName("Daily Routine를 등록한다.")
    void postDailyRoutine() throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime("2023-10-01T22:00:00")
                .endDateTime("2023-10-01T23:00:00")
                .routineType("STUDY")
                .routineDescription("자바 스터디")
                .build();

        BDDMockito.given(dailyRoutineService.postDailyRoutine(any(), any(), any(), any()))
                .willReturn(
                        List.of(1L)
                );

        //when, then
        mockMvc.perform(
                        post("/api/daily-routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("daily-routine/post-daily-routine"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("시작일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss (※종료일시와 24시간 이상 차이 날 수 없습니다.)")),
                                fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("종료일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss (※시작일시와 24시간 이상 차이 날 수 없습니다.)")),
                                fieldWithPath("routineType").type(JsonFieldType.STRING).description("루틴 타입")
                                        .attributes(field("constraints", allEnumString(RoutineType.class))),
                                fieldWithPath("routineDescription").type(JsonFieldType.STRING).description("루틴 설명")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("데이터 (등록된 TimeBlock ID 목록)")
                        )
                ));
    }

    @Test
    @DisplayName("Daily Routine를 수정한다.")
    void putDailyRoutine() throws Exception {
        //given
        final PostRequest request = PostRequest.builder()
                .startDateTime("2023-10-01T22:00:00")
                .endDateTime("2023-10-01T23:00:00")
                .routineType("STUDY")
                .routineDescription("자바 스터디")
                .build();

        BDDMockito.given(dailyRoutineService.putDailyRoutine(any(), any(), any(), any(), any()))
                .willReturn(
                        1L
                );

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.put("/api/daily-routine/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("daily-routine/put-daily-routine"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("수정할 TimeBlock ID")
                        )
                        , requestFields(
                                fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("시작일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss (※종료일시와 24시간 이상 차이 날 수 없습니다.)")),
                                fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("종료일시")
                                        .attributes(field("constraints", "yyyy-MM-ddTHH:mm:ss (※시작일시와 24시간 이상 차이 날 수 없습니다.)")),
                                fieldWithPath("routineType").type(JsonFieldType.STRING).description("루틴 타입")
                                        .attributes(field("constraints", allEnumString(RoutineType.class))),
                                fieldWithPath("routineDescription").type(JsonFieldType.STRING).description("루틴 설명")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (수정된 TimeBlock ID)")
                        )
                ));
    }

    @Test
    @DisplayName("Daily Routine을 삭제한다.")
    void deleteDailyRoutine() throws Exception {
        //given
        BDDMockito.given(dailyRoutineService.deleteDailyRoutine(any()))
                .willReturn(
                        1L
                );

        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/daily-routine/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber())
                .andDo(document("daily-routine/delete-daily-routine"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("id").description("삭제할 TimeBlock ID")
                        )
                        , responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터 (삭제된 TimeBlock ID)")
                        )
                ));
    }
}
