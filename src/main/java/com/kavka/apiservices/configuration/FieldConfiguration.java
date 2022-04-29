package com.kavka.apiservices.configuration;

import com.kavka.apiservices.common.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldConfiguration {

    @Bean
    @ConfigurationProperties("spring.mail")
    public MailProperties mailProperties() {
        return new MailProperties();
    }
}
