package com.github.pmoerenhout;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class SwaggerUiWebMvcConfigurer implements WebMvcConfigurer {

  final String baseUrl = "";

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(baseUrl + "/swagger-ui/")
        .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
  }
}
