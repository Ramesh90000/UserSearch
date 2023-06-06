package com.user.search.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

/**
 * Reference Links :
 * <a href="https://springdoc.org/faq.html#how-can-i-configure-swagger-ui">Customize Swagger UI</a>
 * <a href="https://stackoverflow.com/questions/59291371/migrating-from-springfox-swagger-2-to-springdoc-open-api">StackOverflow Link</a>
 */

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI awesomeAPI() {
        return new OpenAPI()
                .info(new Info().title("User Data")
                        .description("UserSearch Description")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Vinay, vinay@gmail.com"));
    }

}