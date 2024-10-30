package snippet

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

/* Clase de configuración para definir y configurar beans
(objetos administrados por Spring) que pueden ser reutilizados */

@Configuration
class AppConfig {
    // Definimos RestTemplate como un bean para inyectarlo en otras clases
    // RestTemplate es una clase que sirve como cliente HTTP (permite hacer solicitudes HTTP)
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
