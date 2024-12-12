import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
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
import snippet.services.UserService
import java.util.Optional

class SnippetServiceTests {
    private lateinit var snippetService: SnippetService
    private lateinit var snippetRepositoryMock: SnippetRepository
    private lateinit var assetServiceMock: AssetService
    private lateinit var permissionServiceMock: PermissionService
    private lateinit var printscriptServiceMock: PrintscriptService
    private lateinit var userServiceMock: UserService

    @BeforeEach
    fun setUp() {
        snippetRepositoryMock = mock(SnippetRepository::class.java)
        assetServiceMock = mock(AssetService::class.java)
        permissionServiceMock = mock(PermissionService::class.java)
        printscriptServiceMock = mock(PrintscriptService::class.java)
        userServiceMock = mock(UserService::class.java)

        snippetService =
            SnippetService(
                snippetRepositoryMock, assetServiceMock, permissionServiceMock,
                printscriptServiceMock, userServiceMock,
            )
    }

    @Test
    fun `deleteSnippet should not throw exception`() {
        val snippet = Snippet()

        `when`(snippetRepositoryMock.findById(anyLong())).thenReturn(Optional.of(snippet))

        assertDoesNotThrow {
            snippetService.deleteSnippet("userId", 1L)
        }
    }

    @Test
    fun `getUsers should return paginated users`() {
        val users = listOf("user1", "user2")
        val pageNumber = 0
        val pageSize = 10

        `when`(permissionServiceMock.getUsers()).thenReturn(users)

        val result = snippetService.getUsers(pageNumber, pageSize)

        assertEquals(PageImpl(users, PageRequest.of(pageNumber, pageSize), users.size.toLong()), result)
    }

    @Test
    fun `getSnippets should return paginated snippets`() {
        val snippets = listOf(Snippet())
        val resources = listOf(PermissionResponse("1", setOf(Permission.READ, Permission.WRITE)))
        val pageRequest = PageRequest.of(0, 10)

        `when`(permissionServiceMock.getAlluserResources(anyString())).thenReturn(resources)
        `when`(snippetRepositoryMock.findAllById(anyList())).thenReturn(snippets)
        `when`(assetServiceMock.getSnippet(anyString())).thenReturn("content")

        val result = snippetService.getSnippets("authorId", 0, 10)

        assertEquals(
            PageImpl(snippets.map { GetSnippetDto.from(it, "content") }, pageRequest, snippets.size.toLong()),
            result,
        )
    }

    @Test
    fun `getSnippetById should return snippet`() {
        val snippet = Snippet()
        val resources = listOf(PermissionResponse("1", setOf(Permission.READ, Permission.WRITE)))

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
        `when`(permissionServiceMock.userCanWrite(anyString(), anyString())).thenReturn(
            UserResource(
                "1",
                listOf(Permission.WRITE),
            ),
        )
        `when`(printscriptServiceMock.validate(anyString())).thenReturn(ValidationResult(true, "", 0, 0))

        val result = snippetService.updateSnippet("userId", updateSnippetDto, "correlationId")

        assertEquals(GetSnippetDto.from(snippet, "new content"), result)
    }
}
