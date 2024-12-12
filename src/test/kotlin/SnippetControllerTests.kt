import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.data.domain.PageImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import snippet.controllers.SnippetController
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.ShareSnippetDTO
import snippet.security.JwtUtil
import snippet.services.Auth0Service
import snippet.services.SnippetService

class SnippetControllerTests {
    private lateinit var snippetService: SnippetService
    private lateinit var auth0Service: Auth0Service
    private lateinit var jwtUtil: JwtUtil
    private lateinit var snippetController: SnippetController
    private lateinit var jwtMock: Jwt

    @BeforeEach
    fun setUp() {
        snippetService = mock(SnippetService::class.java)
        auth0Service = mock(Auth0Service::class.java)
        jwtUtil = mock(JwtUtil::class.java)
        snippetController = SnippetController(snippetService, auth0Service, jwtUtil)
        jwtMock = mock(Jwt::class.java)
    }

    @Test
    fun `getSnippets should return snippets`() {
        val snippets = PageImpl(listOf(mock(GetSnippetDto::class.java)))
        `when`(jwtMock.subject).thenReturn("userId")
        `when`(snippetService.getSnippets(anyString(), anyInt(), anyInt())).thenReturn(snippets)

        val response = snippetController.getSnippets(0, 10, jwtMock)

        assertEquals(snippets, response)
    }

    @Test
    fun `deleteSnippet should call deleteSnippet service`() {
        `when`(jwtMock.subject).thenReturn("userId")

        snippetController.deleteSnippet("1", jwtMock)

        verify(snippetService).deleteSnippet("userId", 1L)
    }

    @Test
    fun `shareSnippet should return UserResourcePermission`() {
        val snippetFriend = ShareSnippetDTO("1", "friendUsername")
        val userResourcePermission = mock(UserResourcePermission::class.java)
        `when`(jwtMock.subject).thenReturn("userId")
        `when`(snippetService.shareSnippet(anyString(), anyString(), anyLong())).thenReturn(userResourcePermission)

        val response = snippetController.shareSnippet(snippetFriend, jwtMock)

        assertEquals(ResponseEntity.ok(userResourcePermission), response)
    }

    @Test
    fun `getUsers should return paginated users`() {
        val users = PageImpl(listOf("user1", "user2"))
        `when`(snippetService.getUsers(anyInt(), anyInt())).thenReturn(users)

        val response = snippetController.getUsers(0, 10)

        assertEquals(users, response)
    }
}
