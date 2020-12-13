package com.github.pmoerenhout;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class ApiController {

  private static final Logger log = LoggerFactory.getLogger(ApiController.class);

  @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public StringResponse doGet() throws IOException {
    log.info("Get is called");
    return new StringResponse("Get");
  }

  @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public StringResponse doPost() throws IOException {
    log.info("Post is called");
    return new StringResponse("Post");
  }


}
