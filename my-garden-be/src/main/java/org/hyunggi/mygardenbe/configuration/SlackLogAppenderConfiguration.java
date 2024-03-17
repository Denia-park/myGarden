package org.hyunggi.mygardenbe.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Slack 관련 Bean 생성 Configuration
 */
@Profile("prod2")
@Configuration
public class SlackLogAppenderConfiguration {
    /**
     * 메세지를 전송할 Slack 채널의 주소 Token
     */
    @Value("${my-garden.slack.incoming-webhook-token}")
    String token;

    /**
     * SlackApi Bean
     * <br><br>
     * - SlackApi Bean 생성
     *
     * @return SlackApi
     */
    @Bean
    public SlackApi slackApi() {
        return new SlackApi("https://hooks.slack.com/services/" + token);
    }
}
