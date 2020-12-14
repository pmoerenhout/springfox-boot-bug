### Springfox Boot Starter and /** request mapping

This code demonstrates the Swagger UI request failure when an /** request mapping is used in a Spring controller.
It can be worked around to use more describing patterns like /prefix/** but doesn'' work when a handle all mapping is required.

The /** mapping can be replaced with an mapping which excludes the /swagger-ui/ patterns:
```
@RequestMapping(value = { "/", "/{part:^(?!swagger-ui$).*}/**" })
```

I believe the Spring dispatcher select the /** RequestMappingHandlerMapping first before the SimpleUrlHandlerMapping with the Swagger URL patterns.
The SwaggerDispatcherServlet works around it by selecting it earlier.
You can enable the use of the SwaggerDispatcherServlet in the Application.java.
```
@Bean
  public DispatcherServlet dispatcherServlet() {
    final SwaggerDispatcherServlet dispatcherServlet = new SwaggerDispatcherServlet();
    dispatcherServlet.setSwaggerBaseUrl(swaggerBaseUrl);
    return dispatcherServlet;
  }
```

Pim Moerenhout
pim.moerenhout (at) gmail.com
