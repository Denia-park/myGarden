package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

/**
 * Server Error Detect Controller Advice
 * <br><br>
 * - 서버 에러 발생 시 Slack으로 알림을 보내는 Controller Advice
 * - Profile이 prod일 때만 동작
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Profile("prod")
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ServerErrorDetectControllerAdvice {
    /**
     * Slack API Bean
     */
    private final SlackApi slackApi;

    /**
     * Exception 발생 시 Slack으로 알림을 보냄
     *
     * @param req HttpServletRequest
     * @param e   Exception
     * @throws Exception Exception
     */
    @ExceptionHandler(Exception.class)
    public void handleException(final HttpServletRequest req, final Exception e) throws Exception {
        log.error("Server Error : {}", e.getMessage(), e);

        slackApi.call(buildSlackMessage(buildSlackAttachment(req, e)));
        throw e;
    }

    /**
     * Slack Message 생성
     *
     * @param slackAttachment Slack Attachment
     * @return Slack Message
     */
    private SlackMessage buildSlackMessage(final SlackAttachment slackAttachment) {
        final SlackMessage slackMessage = new SlackMessage();

        slackMessage.setAttachments(List.of(slackAttachment));
        slackMessage.setIcon(":ghost:");
        slackMessage.setText("Error Detect");
        slackMessage.setUsername("error-bot");

        return slackMessage;
    }

    /**
     * Slack Attachment 생성
     *
     * @param req HttpServletRequest
     * @param e   Exception
     * @return Slack Attachment
     */
    private SlackAttachment buildSlackAttachment(final HttpServletRequest req, final Exception e) {
        final SlackAttachment slackAttachment = new SlackAttachment();

        slackAttachment.setFallback("Error");
        slackAttachment.setColor("danger");
        slackAttachment.setTitle("Server Error Detect");
        slackAttachment.setTitleLink(req.getContextPath());
        slackAttachment.setText(getStackTrace(e));
        slackAttachment.setColor("danger");
        slackAttachment.setFields(addField(req));

        return slackAttachment;
    }

    /**
     * Exception Stack Trace를 String으로 변환
     *
     * @param e Exception
     * @return Stack Trace
     */
    private String getStackTrace(final Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

    /**
     * Slack Attachment Field 추가
     *
     * @param req HttpServletRequest
     * @return Slack Attachment Field
     */
    private List<SlackField> addField(final HttpServletRequest req) {
        return List.of(
                new SlackField().setTitle("Request URL").setValue(req.getRequestURL().toString()),
                new SlackField().setTitle("Request Method").setValue(req.getMethod()),
                new SlackField().setTitle("Request Time").setValue(new Date().toString()),
                new SlackField().setTitle("JWT Token").setValue(req.getHeader("Authorization")),
                new SlackField().setTitle("Request IP").setValue(req.getRemoteAddr()),
                new SlackField().setTitle("Request User-Agent").setValue(req.getHeader("User-Agent"))
        );
    }
}
