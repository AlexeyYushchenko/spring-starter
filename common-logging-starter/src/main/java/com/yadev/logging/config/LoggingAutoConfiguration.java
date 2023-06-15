package com.yadev.logging.config;

import com.yadev.logging.aop.CommonPointcuts;
import com.yadev.logging.aop.FirstAspect;
import com.yadev.logging.aop.SecondAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    static {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnMissingBean
    public CommonPointcuts commonPointcuts() {
        return new CommonPointcuts();
    }

    @Bean
    @Order(1)
    @ConditionalOnMissingBean
    public FirstAspect firstAspect() {
        return new FirstAspect();
    }

    @Bean
    @Order(2)
    @ConditionalOnMissingBean
    public SecondAspect secondAspect() {
        return new SecondAspect();
    }








}