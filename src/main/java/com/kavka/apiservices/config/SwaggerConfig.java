package com.kavka.apiservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * This is swagger configuration class
 * To access the swagger API docs - http://address/v2/api-docs
 * To access the swagger ui use -  http://address/swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kavka.apiservices"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Orderdesk helper API",
                "Middle layer between front and orderdesk. Handles in beteween tasks.",
                "1.0",
                "https://kavkadesigns.com/",
                new Contact("Samyam Acharya", "https://samyam.com", "therealsamyam@gmail.com"),
                "Orderdesk-Helper Copyright@2021 License",
                "https://orderdesk-helper-license.com",
                Collections.emptyList()
        );
    }
}
