package snippet

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CORSConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173")  // Your frontend URL
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Ensure OPTIONS is allowed
            .allowedHeaders("Content-Type", "Authorization")  // Explicitly allow necessary headers
            .allowCredentials(true)  // If credentials like cookies or tokens are needed
    }
}
