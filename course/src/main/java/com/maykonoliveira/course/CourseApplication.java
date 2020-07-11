package com.maykonoliveira.course;

import com.maykonoliveira.core.properties.JwtConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.maykonoliveira")
@EntityScan({"com.maykonoliveira.core.entities"})
@EnableJpaRepositories({"com.maykonoliveira.core.repository"})
@EnableConfigurationProperties(value = JwtConfiguration.class)
public class CourseApplication {

  public static void main(String[] args) {
    SpringApplication.run(CourseApplication.class, args);
  }
}
