package com.maykonoliveira.course.docs;

import com.maykonoliveira.core.docs.BaseSwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfiguration {
  public SwaggerConfig() {
    super("com.maykonoliveira.course.endpoint.controller");
  }
}
