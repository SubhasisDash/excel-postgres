package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static springfox.documentation.builders.PathSelectors.regex;


import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class TesterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesterProjectApplication.class, args);
	}
	@Bean
	public Docket personApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("spring-swagger-api").apiInfo(apiInfo()).select()
				.paths(regex("/Excel.*")).build();
	}

    private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Spring REST Sample with Swagger")
				.description("By Milan & Subhasis").license("0")
				.licenseUrl("").version("1.0").build();
	}
}
