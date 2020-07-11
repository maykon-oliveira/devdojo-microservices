package com.maykonoliveira.auth.docs;

import com.maykonoliveira.core.docs.BaseSwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** @author maykon-oliveira */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfiguration {
  public SwaggerConfig() {
    super("com.maykonoliveira.auth.endpoint.controller");
  }
}
