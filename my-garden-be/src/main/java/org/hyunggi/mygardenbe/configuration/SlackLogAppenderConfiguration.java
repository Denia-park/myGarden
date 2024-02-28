package org.hyunggi.mygardenbe.configuration;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class SlackLogAppenderConfiguration {
    @Value("${my-garden.slack.incoming-webhook-token}")
    String token;

    @Bean
    public SlackApi slackApi() {
        return new SlackApi("https://hooks.slack.com/services/" + token);
    }
}
