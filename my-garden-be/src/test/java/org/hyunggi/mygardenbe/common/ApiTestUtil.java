package org.hyunggi.mygardenbe.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyunggi.mygardenbe.auth.controller.request.LoginRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ApiTestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ApiTestUtil() {
        //Utility class
    }

    public static void signUp(final MockMvc mockMvc, final String email, final String password) throws Exception {
        final String content = objectMapper.writeValueAsString(
                LoginRequest.builder()
                        .email(email)
                        .password(password)
                        .build()
        );

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
    }
}
