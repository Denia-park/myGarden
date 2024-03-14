package org.hyunggi.mygardenbe.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus Configuration
 * <br><br>
 * - Prometheus 설정
 * - spring.security, spring.data.repository로 시작하는 metric은 제외
 */
@Configuration
public class PrometheusConfiguration {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .meterFilter(MeterFilter.deny(id -> id.getName().startsWith("spring.security")))
                .meterFilter(MeterFilter.deny(id -> id.getName().startsWith("spring.data.repository")));
    }
}
