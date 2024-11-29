package snippet

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CORSConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Match all endpoints
            .allowedOrigins("http://localhost:5173")
            .allowedHeaders("Authorization", "Content-Type", "ngrok-skip-browser-warning")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow OPTIONS for preflight
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true) // Allow credentials
            .maxAge(1800)

    }
}
