package com.github.pmoerenhout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${springfox.documentation.swagger-ui.base-url:}")
  private String swaggerBaseUrl;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    // Redirect the old URL to the new
    registry.addRedirectViewController("/swagger-ui.html", swaggerBaseUrl + "/swagger-ui/index.html");
  }

}