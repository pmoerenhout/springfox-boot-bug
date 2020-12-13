### Springfox Boot Starter and /** request mapping

This code demonstrates the Swagger UI request failure when an /** request mapping with some other annotations like method, or consumes, is used in a controller.
It can be worked around to use more describing patterns like /prefix/** or just use @RequestMapping("\**").

I believe the Spring dispatcher select the /** handler first before the SimpleUrlHandlerMapping with the Swagger URL patterns.
The SwaggerDispatcherServlet works around it by selecting it earlier.

The other workaround is the just use @RequestMapping("\**").

Pim Moerenhout
pim.moerenhout (at) gmail.com
