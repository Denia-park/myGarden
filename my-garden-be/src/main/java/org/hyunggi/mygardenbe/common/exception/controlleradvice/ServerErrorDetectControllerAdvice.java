package org.hyunggi.mygardenbe.common.exception.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

@Profile("prod")
@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ServerErrorDetectControllerAdvice {
    private final SlackApi slackApi;

    @ExceptionHandler(Exception.class)
    public void handleException(final HttpServletRequest req, final Exception e) throws Exception {
        log.error("Server Error : {}", e.getMessage(), e);

        slackApi.call(buildSlackMessage(buildSlackAttachment(req, e)));
        throw e;
    }

    private SlackMessage buildSlackMessage(final SlackAttachment slackAttachment) {
        final SlackMessage slackMessage = new SlackMessage();

        slackMessage.setAttachments(List.of(slackAttachment));
        slackMessage.setIcon(":ghost:");
        slackMessage.setText("Error Detect");
        slackMessage.setUsername("error-bot");

        return slackMessage;
    }

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

    private String getStackTrace(final Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

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
