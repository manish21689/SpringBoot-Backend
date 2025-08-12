package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("BackEnd-API")
				.description("API documentation for my Spring Boot application").version("1.0.0"));
	}
}

    //run application and check below route    
	// http://localhost:8080/swagger-ui.html
	// http://localhost:8080/swagger-ui/index.html
