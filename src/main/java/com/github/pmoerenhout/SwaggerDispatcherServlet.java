package com.github.pmoerenhout;

import static springfox.documentation.builders.BuilderDefaults.nullToEmpty;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

public class SwaggerDispatcherServlet extends DispatcherServlet {

  private static final Logger log = LoggerFactory.getLogger(SwaggerDispatcherServlet.class);

  private String swaggerBaseUrl = "";

  public SwaggerDispatcherServlet() {
    super();
  }

  public SwaggerDispatcherServlet(final WebApplicationContext webApplicationContext) {
    super(webApplicationContext);
  }

  @Override
  protected HandlerExecutionChain getHandler(final HttpServletRequest request) throws Exception {
    List<HandlerMapping> handlerMappings = getHandlerMappings();
    log.info("Found " + handlerMappings.size() + " handlerMappings " + handlerMappings);
    if (handlerMappings != null) {
      logger.debug("$$$$$$$$$$ " + swaggerBaseUrl + "/swagger-ui/");
      if (request.getRequestURI().startsWith(swaggerBaseUrl + "/swagger-ui/")) {
        logger.debug("It's a Swagger URL");
        for (HandlerMapping mapping : handlerMappings) {
          if (mapping instanceof SimpleUrlHandlerMapping) {
            logger.debug("Try SimpleUrlHandler mapping " + mapping);
            HandlerExecutionChain handler = mapping.getHandler(request);
            if (handler != null) {
              logger.info("Match! " + handler);
              return handler;
            }
          }
        }
      }
      return super.getHandler(request);
    }
    return null;
  }

  public void setSwaggerBaseUrl(final String swaggerBaseUrl) {
    this.swaggerBaseUrl = fixup(swaggerBaseUrl);
  }

  private String fixup(String swaggerBaseUrl) {
    return StringUtils.trimTrailingCharacter(nullToEmpty(swaggerBaseUrl), '/');
  }
}
