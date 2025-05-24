package com.hachicore.demo.jooq.config;

import org.jooq.conf.ExecuteWithoutWhere;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer() {
        return config -> config
                .set(PerformanceListener::new)
                .settings()
                .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)
                .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW)
                .withRenderSchema(false);
    }

}
