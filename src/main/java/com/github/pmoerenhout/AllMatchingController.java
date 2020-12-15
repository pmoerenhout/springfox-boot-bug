package com.github.pmoerenhout;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllMatchingController {

  private static final Logger log = LoggerFactory.getLogger(AllMatchingController.class);

  @GetMapping(value = "/keepalive")
  public void keepAlive(final HttpServletResponse response) throws IOException {

    final ServletOutputStream outputStream = response.getOutputStream();

    response.setHeader(CONTENT_TYPE, "text/html; charset=utf-8");

    outputStream.write("<html>\r\n".getBytes());
    outputStream.write("<title>Keepalive</title>\r\n".getBytes());
    outputStream.write("<body>\r\n".getBytes());
    outputStream.write(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("UTC"))).getBytes(StandardCharsets.UTF_8));
    outputStream.write("\r\n".getBytes());
    outputStream.write("</body>\r\n".getBytes());
    outputStream.write("</html>".getBytes());
  }

  // The Swagger UI works when a URL matching pattern is used which doesn't match the /swagger-ui URL's.
  // @RequestMapping("/any/**")
  // @PostMapping("/any/**")

  // The Swagger UI breaks when an URL matching pattern is used which also matches the Swagger UI URL's.
  // It works only if the /swagger-ui request are handled by the SimpleUrlHandlerMapping, see the SwaggerDispatcherServlet workaround.
  // @RequestMapping(value = "/**") // Swagger-UI doesn't works
  // @PostMapping(value = "/**") // Swagger-UI doesn't works

  // @RequestMapping(value = { "/", "/{part:^(?!swagger-ui$).*}/**" }) // Swagger-UI works

  @RequestMapping(value = { "", "{part:^(?!^swagger-ui$).*}/**" })
  public void anyRequest(
      @PathVariable(required = false) final String part,
      final HttpServletRequest request,
      final HttpServletResponse response)
      throws IOException {

    log.info("Method:{} URI:{} part:{}", request.getRequestURI(), request.getMethod(), part);

    final ServletOutputStream outputStream = response.getOutputStream();

    final String requestUriQueryString;
    if (request.getQueryString() == null) {
      requestUriQueryString = request.getRequestURI();
    } else {
      requestUriQueryString = request.getRequestURI() + "?" + request.getQueryString();
    }

    log.debug("Received {} {} request local:{}:{} remote:{}:{}",
        request.getMethod(), requestUriQueryString,
        request.getLocalAddr(), request.getLocalPort(),
        request.getRemoteAddr(), request.getRemotePort());
    if (log.isTraceEnabled()) {
      final Enumeration<String> enumeration = request.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        final String attributeName = enumeration.nextElement();
        log.trace("Received attribute {}: {}", attributeName, request.getAttribute(attributeName));
      }
    }

    outputStream.write("<html>\r\n".getBytes(StandardCharsets.UTF_8));
    outputStream.write("<body>\r\n".getBytes(StandardCharsets.UTF_8));
    outputStream.write("This is the anyRequest".getBytes(StandardCharsets.UTF_8));
    outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    outputStream.write("</body>\r\n".getBytes(StandardCharsets.UTF_8));
    outputStream.write("</html>".getBytes(StandardCharsets.UTF_8));
    return;
  }

}
