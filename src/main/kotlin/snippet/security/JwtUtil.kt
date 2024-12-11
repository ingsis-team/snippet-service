package snippet.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JwtUtil {
    fun getAuthorIdFromToken(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as Jwt
        return jwt.claims["sub"] as String // Assuming `sub` claim contains the authorId
    }
}
