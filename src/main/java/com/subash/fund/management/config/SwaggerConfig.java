package com.subash.fund.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration class for enabling OpenAPI documentation
 * and Swagger UI in the Spring Boot application.
 *
 * <p>This configuration sets up a custom {@link OpenAPI} bean to define
 * API metadata such as title, description, version, and contact information.</p>
 *
 * <p>It also defines a {@link GroupedOpenApi} bean to group and filter
 * the documented APIs under the base path <code>/v1/api/**</code>.</p>
 *
 * <p>Once configured, Swagger UI will be available at:
 * <code>http://localhost:8080/swagger-ui.html</code></p>
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates the OpenAPI definition for the User Management API.
     *
     * @return {@link OpenAPI} instance containing general API metadata
     */
    @Bean
    public OpenAPI userManagementOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("User Management API")
                .description("Spring Boot REST API for managing users")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Subashchandar S")
                        .email("subash12396@gmail.com")
                        .url("https://github.com/subishsubash")));
    }

    /**
     * Creates a grouped OpenAPI bean to group endpoints matching the specified path.
     *
     * @return {@link GroupedOpenApi} instance for user-management endpoints
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("user-management")
                .pathsToMatch("/v1/api/**")
                .build();
    }
}

