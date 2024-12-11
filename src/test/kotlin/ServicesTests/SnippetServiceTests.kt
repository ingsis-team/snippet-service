

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import snippet.model.dtos.permission.Permission
import snippet.model.dtos.permission.PermissionResponse
import snippet.model.dtos.permission.UserResource
import snippet.model.dtos.printscript.ValidationResult
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.model.entities.Snippet
import snippet.repositories.SnippetRepository
import snippet.services.AssetService
import snippet.services.PermissionService
import snippet.services.PrintscriptService
import snippet.services.SnippetService
import java.util.Optional

class SnippetServiceTests {
    private lateinit var snippetService: SnippetService
    private lateinit var snippetRepositoryMock: SnippetRepository
    private lateinit var assetServiceMock: AssetService
    private lateinit var permissionServiceMock: PermissionService
    private lateinit var printscriptServiceMock: PrintscriptService

    @BeforeEach
    fun setUp() {
        snippetRepositoryMock = mock(SnippetRepository::class.java)
        assetServiceMock = mock(AssetService::class.java)
        permissionServiceMock = mock(PermissionService::class.java)
        printscriptServiceMock = mock(PrintscriptService::class.java)

        snippetService = SnippetService(snippetRepositoryMock, assetServiceMock, permissionServiceMock, printscriptServiceMock)
    }
/*
    @Test
    fun `createSnippet should return saved snippet`() {
        val snippetDto = SnippetCreateDto("snippet1", "printscript", "content", "compliant", "kt", "username")
        val snippet = Snippet.from(snippetDto, "authorId", "username")
        val savedSnippet = Snippet()

        `when`(snippetRepositoryMock.save(any(Snippet::class.java))).thenReturn(savedSnippet)
        `when`(printscriptServiceMock.validate(anyString())).thenReturn(ValidationResult(true, "", 0, 0))

        val result = snippetService.createSnippet(snippetDto, "correlationId", "authorId", "username")

        assertEquals(savedSnippet, result)
    }

 */

    @Test
    fun `getSnippets should return paginated snippets`() {
        val snippets = listOf(Snippet())
        val resources = listOf(PermissionResponse("1", setOf(Permission.READ, Permission.WRITE))) // Use numeric string for ID
        val pageRequest = PageRequest.of(0, 10)

        `when`(permissionServiceMock.getAlluserResources(anyString())).thenReturn(resources)
        `when`(snippetRepositoryMock.findAllById(anyList())).thenReturn(snippets)
        `when`(assetServiceMock.getSnippet(anyString())).thenReturn("content")

        val result = snippetService.getSnippets("authorId", 0, 10)

        assertEquals(PageImpl(snippets.map { GetSnippetDto.from(it, "content") }, pageRequest, snippets.size.toLong()), result)
    }

    @Test
    fun `getSnippetById should return snippet`() {
        val snippet = Snippet()
        val resources = listOf(PermissionResponse("1", setOf(Permission.READ, Permission.WRITE))) // Use numeric string for ID

        `when`(snippetRepositoryMock.findById(anyLong())).thenReturn(Optional.of(snippet))
        `when`(permissionServiceMock.getAlluserResources(anyString())).thenReturn(resources)
        `when`(assetServiceMock.getSnippet(anyString())).thenReturn("content")

        val result = snippetService.getSnippetById("username", 1L)

        assertEquals(GetSnippetDto.from(snippet, "content"), result)
    }

    @Test
    fun `updateSnippet should return updated snippet`() {
        val snippet = Snippet()
        val updateSnippetDto = UpdateSnippetDto("1", "new content")

        `when`(snippetRepositoryMock.findById(anyLong())).thenReturn(Optional.of(snippet))
        `when`(permissionServiceMock.userCanWrite(anyString(), anyString())).thenReturn(UserResource("1", listOf(Permission.WRITE)))
        `when`(printscriptServiceMock.validate(anyString())).thenReturn(ValidationResult(true, "", 0, 0))

        val result = snippetService.updateSnippet("userId", updateSnippetDto, "correlationId")

        assertEquals(GetSnippetDto.from(snippet, "new content"), result)
    }
/*
    @Test
    fun `deleteSnippet should not throw exception`() {
        val snippet = Snippet()

        `when`(snippetRepositoryMock.findById(anyLong())).thenReturn(Optional.of(snippet))

        assertDoesNotThrow {
            snippetService.deleteSnippet("userId", 1L)
        }
    }

    @Test
    fun `formatSnippet should return formatted snippet`() {
        val snippet = Snippet()
        val formatFileDto = FormatFileDto(UUID.randomUUID(), "1", "language", "1.1", "content", "userId")
        val response = PrintscriptResponseDTO(UUID.randomUUID().toString(), "1", "formattedContent")

        `when`(permissionServiceMock.userCanWrite(anyString(), anyString())).thenReturn(UserResource("1", listOf(Permission.WRITE)))
        `when`(assetServiceMock.getSnippet(anyString())).thenReturn("content")
        `when`(printscriptServiceMock.formatSnippet(any(FormatFileDto::class.java))).thenReturn(response)

        val result = snippetService.formatSnippet("userId", "1", "language", UUID.randomUUID())

        assertEquals("formattedContent", result)
    }

 */
}
