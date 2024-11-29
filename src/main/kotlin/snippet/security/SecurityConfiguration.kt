package snippet.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
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
    @Value("\${auth0.audience}") private val audience: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuer: String,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it
                .requestMatchers("/").permitAll() // Allow root path without authentication
                .requestMatchers(HttpMethod.GET, "/snippets").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(HttpMethod.GET, "/snippets/*").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(HttpMethod.POST, "/snippets").hasAuthority("SCOPE_write:snippets")
                .anyRequest().authenticated() // Require authentication for other requests
        }
            .oauth2ResourceServer { it.jwt() }
            .cors { it.configurationSource {
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

        // Add custom audience validation
        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)

        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}
