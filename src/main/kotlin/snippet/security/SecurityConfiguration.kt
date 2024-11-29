package snippet.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    @Value("\${auth0.audience}")
    val audience: String,
    @Value("\${auth0.issuerUrl}")
    val issuer: String,
) {
    @Bean // define reglas de autorización para solicitudes HTTP
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it
                .requestMatchers("/").permitAll() // permite acceso a ruta raíz sin autenticación
                .requestMatchers(GET, "/snippets").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(GET, "/snippets/*").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(POST, "/snippets").hasAuthority("SCOPE_write:snippets")
                .anyRequest().authenticated() // cualquier otra solicitud requiere autenticación
        }
            .oauth2ResourceServer { it.jwt(withDefaults()) }
            .cors { it.configurationSource { request ->
                val cors = org.springframework.web.cors.CorsConfiguration()
                cors.allowedOrigins = listOf("http://localhost:5173")
                cors.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                cors.allowedHeaders = listOf("*")
                cors.allowCredentials = true
                cors
            } }
            .csrf { it.disable() }
        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuer).build()
        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}
