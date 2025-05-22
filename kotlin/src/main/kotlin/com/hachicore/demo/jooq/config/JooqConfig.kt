package com.hachicore.demo.jooq.config

import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JooqConfig {

    @Bean
    fun jooqDefaultConfigurationCustomizer(): DefaultConfigurationCustomizer =
        DefaultConfigurationCustomizer { config ->
            config.settings()
                .withRenderSchema(false)
        }

}