package com.github.pmoerenhout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

  private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

  /*
   ** http://springfox.github.io/springfox/docs/current/#docket-spring-java-configuration
   ** http://springfox.github.io/springfox/docs/current/#q13§
   */

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api/.*"))
        .build();
  }

}