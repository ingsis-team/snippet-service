import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.Snippet
import snippet.model.entities.SnippetTest
import snippet.model.entities.User

class ModelEntitiesTests {
    @Test
    fun `test Snippet creation`() {
        val snippet = Snippet()
        snippet.id = 1L
        snippet.name = "Snippet1"
        snippet.compliance = "compliant"
        snippet.language = "Kotlin"
        snippet.extension = ".kt"
        snippet.author = "Author1"
        snippet.username = "User1"
        snippet.content = "fun main() {}"

        assertEquals(1L, snippet.id)
        assertEquals("Snippet1", snippet.name)
        assertEquals("compliant", snippet.compliance)
        assertEquals("Kotlin", snippet.language)
        assertEquals(".kt", snippet.extension)
        assertEquals("Author1", snippet.author)
        assertEquals("User1", snippet.username)
        assertEquals("fun main() {}", snippet.content)
    }

    @Test
    fun `test Snippet from method`() {
        val snippetDto =
            SnippetCreateDto(
                name = "Snippet1",
                compliance = "compliant",
                language = "Kotlin",
                extension = ".kt",
                content = "fun main() {}",
                username = "User1",
            )
        val snippet = Snippet.from(snippetDto, "Author1", "User1")

        assertEquals("Snippet1", snippet.name)
        assertEquals("compliant", snippet.compliance)
        assertEquals("Kotlin", snippet.language)
        assertEquals(".kt", snippet.extension)
        assertEquals("Author1", snippet.author)
        assertEquals("User1", snippet.username)
        assertEquals("fun main() {}", snippet.content)
    }

    @Test
    fun `test SnippetTest creation`() {
        val snippetTest = SnippetTest()
        snippetTest.id = 1L
        snippetTest.snippetId = 1L
        snippetTest.name = "Test Case 1"
        snippetTest.input = listOf("input1", "input2")
        snippetTest.output = listOf("output1", "output2")
        snippetTest.envVars = "envVars1"

        assertEquals(1L, snippetTest.id)
        assertEquals(1L, snippetTest.snippetId)
        assertEquals("Test Case 1", snippetTest.name)
        assertEquals(listOf("input1", "input2"), snippetTest.input)
        assertEquals(listOf("output1", "output2"), snippetTest.output)
        assertEquals("envVars1", snippetTest.envVars)
    }

    @Test
    fun `test SnippetTest from method`() {
        val testCreateDto =
            TestCreateDTO(
                name = "Test Case 1",
                input = mutableListOf("input1", "input2"),
                output = mutableListOf("output1", "output2"),
                id = 1L,
                envVars = "envVars1",
            )
        val snippetTest = SnippetTest.from(testCreateDto)

        assertEquals("Test Case 1", snippetTest.name)
        assertEquals(listOf("input1"), snippetTest.input)
        assertEquals(listOf("output1", "output2"), snippetTest.output)
        assertEquals(1L, snippetTest.snippetId)
        assertEquals("envVars1", snippetTest.envVars)
    }

    @Test
    fun `test User entity creation`() {
        val user =
            User().apply {
                nickname = "testNickname"
                id = "testId"
            }

        assertEquals("testNickname", user.nickname)
        assertEquals("testId", user.id)
    }

    @Test
    fun `test User entity default values`() {
        val user = User()

        assertEquals("", user.nickname)
        assertEquals("", user.id)
    }
}
