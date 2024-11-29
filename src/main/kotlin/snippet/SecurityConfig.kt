package snippet

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() } // Disable CSRF
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow OPTIONS requests for CORS preflight
                    .requestMatchers("/", "/example/**").permitAll() // Allow access to / and /public/**
                    .anyRequest().authenticated() // All other requests require authentication
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    // Optional: Map JWT claims to authorities
                }
            }
        return http.build()
    }
}
