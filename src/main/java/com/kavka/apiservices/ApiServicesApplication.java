package com.kavka.apiservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:messages.properties")
public class ApiServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServicesApplication.class, args);
    }

}
