package com.kavka.apiservices;

import com.kavka.apiservices.config.TestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@PropertySource("classpath:messages.properties")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.kavka.apiservices"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {TestSecurityConfig.class})})
public class ApiServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServicesApplication.class, args);
    }

}
