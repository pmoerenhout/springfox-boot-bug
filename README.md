### Springfox Boot Starter and /** request mapping

This code demonstrates the Swagger UI request failure when an /** request mapping is used somewhere.
It can be worked around to use more describing patterns like /prefix/**.

I beliver the Spring dispatcher select the /** handler first before the SimpleUrlHandlerMapping with the Swagger URL patterns.

The SwaggerDispatcherServlet worksaround it by selecting it earlier.

