package org.hyunggi.mygardenbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.hyunggi.mygardenbe.auth.jwt.service.MyLogoutHandler;
import org.hyunggi.mygardenbe.dailyroutine.controller.DailyRoutineController;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.mock.security.MyCustomTestSecurityConfiguration;
import org.hyunggi.mygardenbe.mock.security.WithMyCustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                DailyRoutineController.class,
        },

        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        },

        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MyCustomTestSecurityConfiguration.class)
        }
)
@WithMyCustomUser
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected DailyRoutineService dailyRoutineService;

    @MockBean
    protected MyLogoutHandler myLogoutHandler;
}
