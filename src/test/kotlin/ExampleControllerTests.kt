import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import snippet.ExampleController

class ExampleControllerTests {
    @Test
    fun `exampleEndpoint should return Hello, World!`() {
        val controller = ExampleController()
        val result = controller.exampleEndpoint()
        assertEquals("Hello, World!", result)
    }
}
