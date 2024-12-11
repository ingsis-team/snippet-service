import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import snippet.model.dtos.permission.Permission
import snippet.model.dtos.permission.PermissionResponse
import snippet.model.dtos.permission.ResourcePermissionCreateDTO
import snippet.model.dtos.permission.ShareResource
import snippet.model.dtos.permission.UserResource
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.printscript.ChangeRulesDto
import snippet.model.dtos.printscript.FormatFileDto
import snippet.model.dtos.printscript.ObjectRules
import snippet.model.dtos.printscript.PrintscriptDataDTO
import snippet.model.dtos.printscript.PrintscriptResponseDTO
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.printscript.ValidationResult
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.ShareSnippetDTO
import snippet.model.dtos.snippet.SnippetContext
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.Snippet
import snippet.model.entities.SnippetTest
import java.util.UUID

class ModelDtoTests {
    // modeldtopermission Tests

    @Test
    fun `test PermissionResponse creation`() {
        val permissions = setOf(Permission.READ, Permission.WRITE)
        val response = PermissionResponse("resource1", permissions)
        assertEquals("resource1", response.resourceId)
        assertEquals(permissions, response.permissions)
    }

    @Test
    fun `test ResourcePermissionCreateDTO creation`() {
        val permissions = listOf(Permission.READ, Permission.WRITE)
        val dto = ResourcePermissionCreateDTO("user1", "resource1", permissions)
        assertEquals("user1", dto.userId)
        assertEquals("resource1", dto.resourceId)
        assertEquals(permissions, dto.permissions)
    }

    @Test
    fun `test ShareResource creation with default permissions`() {
        val shareResource = ShareResource("self1", "other1", "resource1")
        assertEquals("self1", shareResource.selfId)
        assertEquals("other1", shareResource.otherId)
        assertEquals("resource1", shareResource.resourceId)
        assertEquals(listOf(Permission.READ, Permission.WRITE), shareResource.permissions)
    }

    @Test
    fun `test ShareResource creation with custom permissions`() {
        val permissions = listOf(Permission.OWNER)
        val shareResource = ShareResource("self1", "other1", "resource1", permissions)
        assertEquals("self1", shareResource.selfId)
        assertEquals("other1", shareResource.otherId)
        assertEquals("resource1", shareResource.resourceId)
        assertEquals(permissions, shareResource.permissions)
    }

    @Test
    fun `test UserResource creation`() {
        val permissions = listOf(Permission.READ, Permission.WRITE)
        val userResource = UserResource("user1", permissions)
        assertEquals("user1", userResource.userId)
        assertEquals(permissions, userResource.resourceId)
    }

    @Test
    fun `test UserResourcePermission creation`() {
        val permissions = listOf(Permission.READ, Permission.WRITE)
        val userResourcePermission = UserResourcePermission("user1", "resource1", permissions)
        assertEquals("user1", userResourcePermission.userId)
        assertEquals("resource1", userResourcePermission.resourceId)
        assertEquals(permissions, userResourcePermission.permissions)
    }

    // ModelDtoPrintscript Tests

    @Test
    fun `test ChangeRulesDto creation`() {
        val rules = listOf(Rule("1", "Rule1", true, null))
        val snippets = listOf(PrintscriptDataDTO(UUID.randomUUID(), "snippet1", "language1", "1.0", "input1"))
        val correlationId = UUID.randomUUID()
        val dto = ChangeRulesDto("user1", rules, snippets, correlationId)
        assertEquals("user1", dto.userId)
        assertEquals(rules, dto.rules)
        assertEquals(snippets, dto.snippets)
        assertEquals(correlationId, dto.correlationId)
    }

    @Test
    fun `test FormatFileDto creation`() {
        val correlationId = UUID.randomUUID()
        val dto = FormatFileDto(correlationId, "snippet1", "language1", "1.0", "input1", "user1")
        assertEquals(correlationId, dto.correlationId)
        assertEquals("snippet1", dto.snippetId)
        assertEquals("language1", dto.language)
        assertEquals("1.0", dto.version)
        assertEquals("input1", dto.input)
        assertEquals("user1", dto.userId)
    }

    @Test
    fun `test ObjectRules creation`() {
        val rules = listOf(Rule("1", "Rule1", true, null))
        val objectRules = ObjectRules(rules)
        assertEquals(rules, objectRules.rules)
    }

    @Test
    fun `test PrintscriptDataDTO creation`() {
        val correlationId = UUID.randomUUID()
        val dto = PrintscriptDataDTO(correlationId, "snippet1", "language1", "1.0", "input1")
        assertEquals(correlationId, dto.correlationId)
        assertEquals("snippet1", dto.snippetId)
        assertEquals("language1", dto.language)
        assertEquals("1.0", dto.version)
        assertEquals("input1", dto.input)
    }

    @Test
    fun `test PrintscriptResponseDTO creation`() {
        val dto = PrintscriptResponseDTO("correlation1", "snippet1", "snippet")
        assertEquals("correlation1", dto.correlationId)
        assertEquals("snippet1", dto.snippetId)
        assertEquals("snippet", dto.snippet)
    }

    @Test
    fun `test Rule creation`() {
        val rule = Rule("1", "Rule1", true, "value")
        assertEquals("1", rule.id)
        assertEquals("Rule1", rule.name)
        assertEquals(true, rule.isActive)
        assertEquals("value", rule.value)
    }

    @Test
    fun `test ValidationResult creation`() {
        val validationResult = ValidationResult(true, "Rule1", 1, 1)
        assertEquals(true, validationResult.isValid)
        assertEquals("Rule1", validationResult.rule)
        assertEquals(1, validationResult.line)
        assertEquals(1, validationResult.column)
    }

    // modeldtosnippet tests

    @Test
    fun `test GetSnippetDto creation`() {
        val dto =
            GetSnippetDto(
                id = "1",
                name = "Snippet1",
                language = "Kotlin",
                content = "fun main() {}",
                compliance = "compliant",
                extension = ".kt",
                author = "Author1",
                username = "User1",
            )
        assertEquals("1", dto.id)
        assertEquals("Snippet1", dto.name)
        assertEquals("Kotlin", dto.language)
        assertEquals("fun main() {}", dto.content)
        assertEquals("compliant", dto.compliance)
        assertEquals(".kt", dto.extension)
        assertEquals("Author1", dto.author)
        assertEquals("User1", dto.username)
    }

    @Test
    fun `test GetSnippetDto from method`() {
        val snippet = Snippet()
        snippet.id = 1
        snippet.name = "Snippet1"
        snippet.author = "Author1"
        snippet.language = "Kotlin"
        snippet.content = "fun main() {}"
        snippet.compliance = "compliant"
        snippet.extension = ".kt"
        assertEquals("1", snippet.id.toString())
        assertEquals("Snippet1", snippet.name)
        assertEquals("Author1", snippet.author)
        assertEquals("Kotlin", snippet.language)
    }

    @Test
    fun `test ShareSnippetDTO creation`() {
        val dto =
            ShareSnippetDTO(
                snippetId = "1",
                friendUsername = "Friend1",
            )
        assertEquals("1", dto.snippetId)
        assertEquals("Friend1", dto.friendUsername)
    }

    @Test
    fun `test SnippetContext creation`() {
        val context =
            SnippetContext(
                snippetId = "1",
                language = "Kotlin",
            )
        assertEquals("1", context.snippetId)
        assertEquals("Kotlin", context.language)
    }

    @Test
    fun `test SnippetCreateDto creation`() {
        val dto =
            SnippetCreateDto(
                name = "Snippet1",
                language = "Kotlin",
                content = "fun main() {}",
                compliance = "compliant",
                extension = ".kt",
                username = "User1",
            )
        assertEquals("Snippet1", dto.name)
        assertEquals("Kotlin", dto.language)
        assertEquals("fun main() {}", dto.content)
        assertEquals("compliant", dto.compliance)
        assertEquals(".kt", dto.extension)
        assertEquals("User1", dto.username)
    }

    @Test
    fun `test UpdateSnippetDto creation`() {
        val dto =
            UpdateSnippetDto(
                id = "1",
                content = "fun main() {}",
            )
        assertEquals("1", dto.id)
        assertEquals("fun main() {}", dto.content)
    }

    @Test
    fun `test TestCaseReturnDto creation`() {
        val dto = TestCaseReturnDto()
        dto.id = "1"
        dto.name = "Test Case 1"
        dto.input = listOf("input1", "input2")
        dto.output = listOf("output1", "output2")
        dto.envVars = "envVars1"

        assertEquals("1", dto.id)
        assertEquals("Test Case 1", dto.name)
        assertEquals(listOf("input1", "input2"), dto.input)
        assertEquals(listOf("output1", "output2"), dto.output)
        assertEquals("envVars1", dto.envVars)
    }

    @Test
    fun `test TestCaseReturnDto from method`() {
        val snippetTest = SnippetTest()
        snippetTest.id = 1
        snippetTest.name = "Test Case 1"
        snippetTest.input = mutableListOf("input1", "input2")
        snippetTest.output = mutableListOf("output1", "output2")
        snippetTest.envVars = "envVars1"
        assertEquals("1", snippetTest.id.toString())
        assertEquals("Test Case 1", snippetTest.name)
    }

    @Test
    fun `from should convert SnippetTest to TestCaseReturnDto`() {
        // Arrange
        val snippetTest = SnippetTest()
        snippetTest.id = 1
        snippetTest.name = "Test Case 1"
        snippetTest.input = mutableListOf("input1", "input2")
        snippetTest.output = mutableListOf("output1", "output2")
        snippetTest.envVars = "envVar1"

        val testCaseReturnDto = TestCaseReturnDto.from(snippetTest)

        assertEquals("1", testCaseReturnDto.id)
        assertEquals("Test Case 1", testCaseReturnDto.name)
        assertEquals(listOf("input1", "input2"), testCaseReturnDto.input)
        assertEquals(listOf("output1", "output2"), testCaseReturnDto.output)
        assertEquals("envVar1", testCaseReturnDto.envVars)
    }

    @Test
    fun `test TestCreateDTO creation`() {
        val dto =
            TestCreateDTO(
                name = "Test Case 1",
                input = mutableListOf("input1", "input2"),
                output = mutableListOf("output1", "output2"),
                id = 1L,
                envVars = "envVars1",
            )

        assertEquals("Test Case 1", dto.name)
        assertEquals(mutableListOf("input1", "input2"), dto.input)
        assertEquals(mutableListOf("output1", "output2"), dto.output)
        assertEquals(1L, dto.id)
        assertEquals("envVars1", dto.envVars)
    }

    @Test
    fun `from should convert Snippet to GetSnippetDto`() {
        // Arrange
        val snippet = Snippet()
        snippet.id = 1
        snippet.name = "Snippet Name"
        snippet.language = "Kotlin"
        snippet.compliance = "Compliant"
        snippet.extension = "kt"
        snippet.author = "Author Name"
        snippet.username = "username"

        val content = "Snippet content"

        val getSnippetDto = GetSnippetDto.from(snippet, content)

        assertEquals("1", getSnippetDto.id)
        assertEquals("Snippet Name", getSnippetDto.name)
        assertEquals("Kotlin", getSnippetDto.language)
        assertEquals("Snippet content", getSnippetDto.content)
        assertEquals("Compliant", getSnippetDto.compliance)
        assertEquals("kt", getSnippetDto.extension)
        assertEquals("Author Name", getSnippetDto.author)
        assertEquals("username", getSnippetDto.username)
    }
}
