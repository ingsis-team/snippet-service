import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import snippet.security.JwtUtil

class SecurityTests {
    private val audience = "expected_audience"

    @Test
    fun `test JwtUtil getAuthorIdFromToken`() {
        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf("sub" to "authorId"))

        val authentication = Mockito.mock(Authentication::class.java)
        Mockito.`when`(authentication.principal).thenReturn(jwt)

        val securityContext = Mockito.mock(SecurityContext::class.java)
        Mockito.`when`(securityContext.authentication).thenReturn(authentication)

        SecurityContextHolder.setContext(securityContext)

        val jwtUtil = JwtUtil()
        val authorId = jwtUtil.getAuthorIdFromToken()

        assertEquals("authorId", authorId)
    }
}
