package snippet.tests

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import snippet.controllers.SnippetController
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.entities.Snippet
import snippet.services.SnippetService

@WebMvcTest(SnippetController::class)
class SnippetControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var snippetService: SnippetService

    @Test
    @WithMockUser(username = "testuser", roles = ["USER"])
    fun `getSnippetById returns snippet`() {
        val userId = "123"
        val snippetId = "1"
        val snippetContent = "print('Hello, World!')"

        // Create a mock Snippet entity
        val snippet = Snippet().apply {
            id = 1L
            name = "Test Snippet"
            compliance = "Strict"
            language = "Kotlin"
            extension = ".kt"
        }

        // Mock the expected DTO from the service
        val expectedDto = GetSnippetDto.from(snippet, snippetContent)
        `when`(snippetService.getSnippetById(userId, snippetId.toLong())).thenReturn(expectedDto)

        // Mock the Jwt object
        val jwt = mock(Jwt::class.java)
        `when`(jwt.subject).thenReturn("testuser")

        // Perform the test
        mockMvc.perform(get("/snippets/byId")
            .param("userId", userId)
            .param("snippetId", snippetId)
            .header("Authorization", "Bearer mock-token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.name").value("Test Snippet"))
            .andExpect(jsonPath("$.language").value("Kotlin"))
            .andExpect(jsonPath("$.content").value(snippetContent))
            .andExpect(jsonPath("$.compliance").value("Strict"))
            .andExpect(jsonPath("$.extension").value(".kt"))
    }

    @Test
    fun `getSnippetById requires authentication`() {
        mockMvc.perform(get("/snippets/byId")
            .param("userId", "123")
            .param("snippetId", "1"))
            .andExpect(status().isUnauthorized)
    }
}
