package spring.minibanksystem.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SwaggerConfig : WebMvcConfigurer {

    @Bean
    fun customOpenAPI(): OpenAPI {
        // Name configuration for the Bearer Authentication scheme
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            // Configure metadata info displayed at the top of the Swagger UI page
            .info(
                Info()
                    .title("Banking System")
                    .version("1.0")
                    .description("REST API with Interceptor Security Configuration")
            )
            // Define the global security component structure (JWT Bearer Token)
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP) // Set authorization type to HTTP
                        .scheme("bearer")               // Specify 'bearer' scheme for JWT
                        .bearerFormat("JWT")            // Clarify token format as JWT for documentation
                )
            )
    }
}
