package com.github.pmoerenhout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  @Value("${springfox.documentation.swagger-ui.base-url:}")
  private String swaggerBaseUrl;

  public static void main(final String[] args) {

    new SpringApplicationBuilder(Application.class)
        .bannerMode(Banner.Mode.LOG)
        .headless(true)
        .web(WebApplicationType.SERVLET)
        .logStartupInfo(true)
        .run(args);
  }

  @Bean
  public DispatcherServlet dispatcherServlet() {
    final SwaggerDispatcherServlet dispatcherServlet = new SwaggerDispatcherServlet();
    dispatcherServlet.setSwaggerBaseUrl(swaggerBaseUrl);
    return dispatcherServlet;
  }

}

