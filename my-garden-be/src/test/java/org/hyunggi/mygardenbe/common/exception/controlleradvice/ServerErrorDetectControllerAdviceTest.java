package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import net.gpedro.integrations.slack.SlackApi;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

class ServerErrorDetectControllerAdviceTest extends IntegrationTestSupport {
    @MockBean
    private SlackApi slackApi;
    private ServerErrorDetectControllerAdvice serverErrorDetectControllerAdvice;

    @BeforeEach
    void setUp() {
        serverErrorDetectControllerAdvice = new ServerErrorDetectControllerAdvice(slackApi);
    }

    @Test
    @DisplayName("Exception 발생 시 Slack으로 알림을 보냄")
    void handleException() {
        final RuntimeException testException = new RuntimeException("test exception");

        // when
        try {
            serverErrorDetectControllerAdvice.handleException(new MockHttpServletRequest(), testException);
        } catch (Exception ignored) {
        }

        // then
        BDDMockito.then(slackApi).should().call(BDDMockito.any());
    }
}
