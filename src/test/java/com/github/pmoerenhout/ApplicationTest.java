package com.github.pmoerenhout;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springfox.documentation.builders.BuilderDefaults.nullToEmpty;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

  private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

  @LocalServerPort
  private int port;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${springfox.documentation.swagger-ui.base-url:}")
  private String swaggerBaseUrl;

  @Test
  public void test_application() {
    log.info("Local port is {}", port);
  }

  @Test
  public void test_api_calls() {
    Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/api/v1/get",
        String.class)).isEqualTo("{\"response\":\"Get\"}");

    Assertions.assertThat(restTemplate.postForObject("http://localhost:" + port + "/api/v1/post", null,
        String.class)).isEqualTo("{\"response\":\"Post\"}");
  }

  @Test
  public void test_keepalive() throws Exception {
    mockMvc
        .perform(get("/keepalive"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html; charset=utf-8"))
        .andExpect(content().string(startsWith("<html>\r\n<title>Keepalive</title>\r\n<body>")));
  }

  @Test
  public void test_swagger() throws Exception {
    mockMvc
        .perform(get(fixup(swaggerBaseUrl) + "/swagger-ui/index.html"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html"))
        .andExpect(content().string(containsString("<title>Swagger UI</title>")));
  }

  @Test
  public void test_post_request_to_any_url() {

    final HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.valueOf()"application/vnd.globalplatform.card-content-mgt;version=1.0");
    // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("X-Admin-From", "123/456/789");
    headers.set(CONTENT_TYPE, "application/vnd.globalplatform.card-content-mgt;version=1.0");

//    // request body parameters
//    Map<String, Object> map = new HashMap<>();
//    map.put("Parameter", "1");

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, headers);

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/any/request/with_any_url", entity, String.class))
        .contains("This is the anyRequest");

  }

  private String fixup(String swaggerBaseUrl) {
    return StringUtils.trimTrailingCharacter(nullToEmpty(swaggerBaseUrl), '/');
  }
}