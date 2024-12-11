import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import snippet.snippet.CORSConfig

class CORSConfigTests {
    @Test
    fun `test addCorsMappings`() {
        val corsConfig = CORSConfig()
        val registry = CorsRegistry()
        corsConfig.addCorsMappings(registry)

        // Use reflection to access the protected getCorsConfigurations method
        val method = CorsRegistry::class.java.getDeclaredMethod("getCorsConfigurations")
        method.isAccessible = true
        val configurations = method.invoke(registry) as Map<String, CorsConfiguration>
        val mapping = configurations["/**"]

        assertNotNull(mapping)
        assertEquals(listOf("http://localhost:5173"), mapping?.allowedOrigins)
        assertEquals(listOf("GET", "POST", "PUT", "DELETE", "OPTIONS"), mapping?.allowedMethods)
        assertEquals(listOf("*"), mapping?.allowedHeaders)
        assertTrue(mapping?.allowCredentials ?: false)
        assertEquals(1800, mapping?.maxAge)
    }
}
