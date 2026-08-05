package spring.minibanksystem.config

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class WebConfig(
    private val jwtInterceptor: JwtInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/accounts/**")
            .addPathPatterns("/api/transactions/**")
            .addPathPatterns("/api/transfer/**")
            .excludePathPatterns(
                "/api/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            )
    }
}