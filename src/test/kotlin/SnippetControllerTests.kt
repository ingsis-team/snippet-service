import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.oauth2.jwt.Jwt
import snippet.controllers.SnippetController
import snippet.security.JwtUtil
import snippet.services.Auth0Service
import snippet.services.SnippetService

class SnippetControllerTests {
    private lateinit var snippetService: SnippetService
    private lateinit var auth0Service: Auth0Service
    private lateinit var jwtUtil: JwtUtil
    private lateinit var controller: SnippetController

    @BeforeEach
    fun setUp() {
        snippetService = Mockito.mock(SnippetService::class.java)
        auth0Service = Mockito.mock(Auth0Service::class.java)
        jwtUtil = Mockito.mock(JwtUtil::class.java)
        controller = SnippetController(snippetService, auth0Service, jwtUtil)
    }
/*
    @Test
    fun `createSnippet should return created snippet`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val snippetData = SnippetCreateDto("username", "printscript", "content", "compliant", "kt", "userName1")
        val snippet = GetSnippetDto("1", "name","printscript", "content", "compliant", "kt", "author1", "userName1")
        Mockito.`when`(jwt.subject).thenReturn("user")
        assertEquals(ResponseEntity.ok(snippet), controller.createSnippet(snippetData, jwt))

    }

    @Test
    fun `getSnippets should return snippets`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val snippets = PageImpl(listOf(GetSnippetDto("1", "name","printscript", "content", "compliant", "kt", "author1", "userName1")))
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.getSnippets(any(), any(), any())).thenReturn(snippets)

        val result = controller.getSnippets(0, 10, jwt)

        assertEquals(snippets, result)
    }

    @Test
    fun `getSnippetById should return snippet`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val snippet = GetSnippetDto("1", "name","printscript", "content", "compliant", "kt", "author1", "userName1")
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.getSnippetById(any(), any())).thenReturn(snippet)

        val result = controller.getSnippetById("1", jwt)

        assertEquals(snippet, result)
    }



    @Test
    fun `updateSnippet should return updated snippet`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val updateSnippetDto = UpdateSnippetDto("1", "updated content")
        val updatedSnippet = GetSnippetDto("1", "name","printscript", "content", "compliant", "kt", "author1", "userName1")
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.updateSnippet(any(), any(), any())).thenReturn(updatedSnippet)

        val result = controller.updateSnippet(updateSnippetDto, jwt)

        assertEquals(ResponseEntity.ok(updatedSnippet), result)
    }


 */

    @Test
    fun `deleteSnippet should call deleteSnippet service`() {
        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.subject).thenReturn("user")

        controller.deleteSnippet("1", jwt)

        Mockito.verify(snippetService).deleteSnippet("user", 1L)
    }
/*
    @Test
    fun `shareSnippet should return user resource permission`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val snippetFriend = ShareSnippetDTO("friendUsername", "1")
        val permission = UserResourcePermission("userId", "resourceId", )
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.shareSnippet(any(), any(), any())).thenReturn(permission)

        val result = controller.shareSnippet(snippetFriend, jwt)

        assertEquals(permission, result)
    }



    @Test
    fun `getUsers should return users`() {
        val users = PageImpl(listOf("user1", "user2"))
        Mockito.`when`(snippetService.getUsers(any(), any())).thenReturn(users)

        val result = controller.getUsers(0, 10)

        assertEquals(users, result)
    }
/*
    @Test
    fun `getAuth0Users should return auth0 users`() {
        val users = listOf(mapOf("id" to "user1"), mapOf("id" to "user2"))
        Mockito.`when`(auth0Service.getUsers(any(), any())).thenReturn(users)

        val result = controller.getAuth0Users(0, 10)

        assertEquals(ResponseEntity.ok(users), result)
    }

 */

    @Test
    fun `getAllAuth0Users should return all auth0 users`() {
        val users = listOf(mapOf("id" to "user1"), mapOf("id" to "user2"))
        Mockito.`when`(auth0Service.getAllUsers()).thenReturn(users)

        val result = controller.getAllAuth0Users()

        assertEquals(ResponseEntity.ok(users), result)
    }

 */
}
