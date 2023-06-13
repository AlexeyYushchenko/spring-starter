package com.yadev.logging.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.commong.logging", value = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    @PostConstruct
    void init(){
        log.info("LoggingAutoConfiguration initialized");
    }
}
