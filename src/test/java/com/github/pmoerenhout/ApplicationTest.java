package com.github.pmoerenhout;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springfox.documentation.builders.BuilderDefaults.nullToEmpty;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

  private static final String ANY_RESPONSE = "This is the anyRequest";

  @LocalServerPort
  private int port;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${springfox.documentation.swagger-ui.base-url:}")
  private String swaggerBaseUrl;

  @BeforeEach
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

  // DispatcherServlet is not used but TestDispatcherServlet
  @Test
  public void test_swagger_index() throws Exception {
    mockMvc
        .perform(get(fixup(swaggerBaseUrl) + "/swagger-ui/index.html"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html"))
        .andExpect(content().string(containsString("<title>Swagger UI</title>")));
  }

  @Test
  public void test_swagger_forward() throws Exception {
    mockMvc
        .perform(get(fixup(swaggerBaseUrl) + "/swagger-ui/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("/swagger-ui/index.html"));
  }

  @Test
  public void test_post_request_to_home_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_random_simple_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/any", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_random_simple_with_trailing_slash_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/any/", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_random_multiple_paths_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/any/request/with_any_url", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_random_multiple_paths_with_trailing_slash_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/any/request/with_any_url/", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_partially_matching_swagger_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/swagg/", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  @Test
  public void test_post_request_to_matching_with_extra_characters_swagger_url() {

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, new HttpHeaders());

    Assertions.assertThat(restTemplate
        .postForObject("http://localhost:" + port + "/swagger-ui-non-matching/", entity, String.class))
        .contains(ANY_RESPONSE);
  }

  private String fixup(String swaggerBaseUrl) {
    return StringUtils.trimTrailingCharacter(nullToEmpty(swaggerBaseUrl), '/');
  }
}