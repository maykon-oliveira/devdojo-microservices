package com.maykonoliveira.core.docs;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author maykon-oliveira */
public class BaseSwaggerConfiguration {

  private final String basePackage;

  public BaseSwaggerConfiguration(String basePackage) {
    this.basePackage = basePackage;
  }

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(basePackage))
        .build()
        .apiInfo(getMeta());
  }

  public ApiInfo getMeta() {
    return new ApiInfoBuilder().title("A Spring Boot microservice API").build();
  }
}
