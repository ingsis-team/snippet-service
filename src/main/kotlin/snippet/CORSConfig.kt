package snippet.snippet

import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CORSConfig : WebMvcConfigurer {

    @Value("\${FRONTEND_URI}")
    private lateinit var frontendUrl: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Match all endpoints
            .allowedOrigins(frontendUrl) // Dynamically use the FRONTEND_URI environment variable
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow OPTIONS for preflight
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true) // Allow credentials
            .maxAge(1800) // Cache preflight response for 30 minutes
    }
}
