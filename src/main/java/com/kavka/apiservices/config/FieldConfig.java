package com.kavka.apiservices.config;

import com.kavka.apiservices.common.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldConfig {

    @Bean
    @ConfigurationProperties("spring.mail")
    public MailProperties mailProperties() {
        return new MailProperties();
    }
}
