package org.hyunggi.mygardenbe.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusConfiguration {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .meterFilter(MeterFilter.deny(id -> id.getName().startsWith("spring.security")))
                .meterFilter(MeterFilter.deny(id -> id.getName().startsWith("spring.data.repository")));
    }
}
