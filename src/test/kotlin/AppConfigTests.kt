import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import snippet.AppConfig

class AppConfigTests {
    @Test
    fun `restTemplate should return a RestTemplate instance`() {
        val appConfig = AppConfig()
        val restTemplate = appConfig.restTemplate()
        assertNotNull(restTemplate)
    }
}
