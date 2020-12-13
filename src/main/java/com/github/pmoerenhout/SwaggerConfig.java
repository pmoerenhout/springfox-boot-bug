package com.github.pmoerenhout;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.RequestHandlerCombiner;
import springfox.documentation.spring.web.plugins.CustRequestHandlerCombiner;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {

  private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

  /*
   ** http://springfox.github.io/springfox/docs/current/#docket-spring-java-configuration
   ** http://springfox.github.io/springfox/docs/current/#q13§
   */

//  @Bean
//  public DocumentationPluginsBootstrapper bootstrapper(){
//    DocumentationPluginsBootstrapper t = new DocumentationPluginsBootstrapper();
//    return
//  }

  @Bean
  public Docket api() {
    //DocumentationContextBuilder builder = new DocumentationContextBuilder(DocumentationType.SWAGGER_2);
    return new Docket(DocumentationType.SWAGGER_2)
        //.configure(builder).getRequestHandlers()
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api/.*"))
        .build();
  }

  @Bean
  public RequestHandlerCombiner requestHandlerCombiner() {
    return new CustRequestHandlerCombiner();
//    return new RequestHandlerCombiner() {
//      @Override
//      public List<RequestHandler> combine(final List<RequestHandler> source) {
//        log.info("COMBINE {}", source);
//        return null;
//      }
//    };
  }

  @Bean
  public AlternateTypeRuleConvention customizeConvention(TypeResolver resolver) {
    return new AlternateTypeRuleConvention() {
      @Override
      public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
      }

      @Override
      public List<AlternateTypeRule> rules() {
        return Arrays.asList(
//            newRule(User.class, emptyMixin(User.class)),
//            newRule(UserDetail.class, emptyMixin(UserDetail.class)),
//            newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin())),
//            newRule(resolver.resolve(Sort.class), resolver.resolve(sortMixin()))
        );
      }
    };
  }
}