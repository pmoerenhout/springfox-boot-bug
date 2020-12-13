package com.github.pmoerenhout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShowUrlComponent implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(ShowUrlComponent.class);

  @Value("${springfox.documentation.swagger-ui.base-url:}")
  private String swaggerBaseUrl;

  @Value("${server.port:8080}")
  private String serverPort;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("http://localhost:{}{}/swagger-ui/index.html", serverPort, swaggerBaseUrl);
    log.info("http://localhost:{}{}/swagger-ui/", serverPort, swaggerBaseUrl);
    log.info("http://localhost:{}/api/v1/first", serverPort);
    log.info("curl -i -X POST http://localhost:{}/api/v1/second", serverPort);
    log.info("http://localhost:{}/keepalive", serverPort);
    log.info(
        "curl -i -X POST -H \"Content-Type: application/vnd.globalplatform.card-content-mgt;version=1\" http://localhost:{}/this/should/be/handled/by/anyRequest",
        serverPort);

  }
}
