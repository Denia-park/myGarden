package org.hyunggi.mygardenbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyunggi.mygardenbe.auth.controller.AuthenticationController;
import org.hyunggi.mygardenbe.auth.jwt.filter.JwtAuthenticationFilter;
import org.hyunggi.mygardenbe.auth.jwt.service.MyLogoutHandler;
import org.hyunggi.mygardenbe.auth.service.AuthenticationService;
import org.hyunggi.mygardenbe.boards.comment.controller.CommentController;
import org.hyunggi.mygardenbe.boards.comment.service.CommentService;
import org.hyunggi.mygardenbe.boards.common.category.controller.BoardCategoryController;
import org.hyunggi.mygardenbe.boards.common.category.service.BoardCategoryService;
import org.hyunggi.mygardenbe.boards.learn.controller.LearnBoardController;
import org.hyunggi.mygardenbe.boards.learn.service.LearnBoardService;
import org.hyunggi.mygardenbe.boards.notice.controller.NoticeBoardController;
import org.hyunggi.mygardenbe.boards.notice.service.NoticeBoardService;
import org.hyunggi.mygardenbe.dailyroutine.controller.DailyRoutineController;
import org.hyunggi.mygardenbe.dailyroutine.service.DailyRoutineService;
import org.hyunggi.mygardenbe.mock.security.MyCustomTestSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                AuthenticationController.class,
                DailyRoutineController.class,
                NoticeBoardController.class,
                BoardCategoryController.class,
                LearnBoardController.class,
                CommentController.class
        },

        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        },

        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MyCustomTestSecurityConfiguration.class)
        }
)
@AutoConfigureRestDocs
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MyLogoutHandler myLogoutHandler;

    @MockBean
    protected DailyRoutineService dailyRoutineService;

    @MockBean
    protected AuthenticationService authenticationService;

    @MockBean
    protected NoticeBoardService noticeBoardService;

    @MockBean
    protected BoardCategoryService boardCategoryService;

    @MockBean
    protected LearnBoardService learnBoardService;

    @MockBean
    protected CommentService commentService;
}
