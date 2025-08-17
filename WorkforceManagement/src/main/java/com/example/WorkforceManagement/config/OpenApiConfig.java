package com.example.WorkforceManagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI workfmOpenAPI() {
        // Define the security scheme (HTTP Bearer, JWT)
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // If you want the security to be applied globally (every operation),
        // add the SecurityRequirement to the root OpenAPI. If you want to
        // protect only specific endpoints, omit the .addSecurityItem(...) call
        // and annotate protected controllers/methods with @SecurityRequirement.
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)) // remove this line if you prefer to annotate controllers selectively
                .info(new Info()
                        .title("WorkFM API")
                        .description("Workforce management API — Work orders, assignments, authentication")
                        .version("v1.0.0")
                        .contact(new Contact().name("Your Team").email("devteam@example.com"))
                );
    }
}
