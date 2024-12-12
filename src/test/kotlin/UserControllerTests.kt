import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import snippet.controllers.UserController
import snippet.services.UserService

class UserControllerTests {
    private lateinit var userController: UserController
    private lateinit var userServiceMock: UserService
    private lateinit var jwtMock: Jwt

    @BeforeEach
    fun setUp() {
        userServiceMock = mock(UserService::class.java)
        jwtMock = mock(Jwt::class.java)
        userController = UserController(userServiceMock)
    }

    @Test
    fun `checkOrCreateUser should create user if nickname does not exist`() {
        val nickname = "testNickname"
        val id = "testId"

        `when`(jwtMock.subject).thenReturn(id)
        `when`(userServiceMock.nicknameExists(nickname)).thenReturn(false)

        val response = userController.checkOrCreateUser(nickname, jwtMock)

        assertEquals(ResponseEntity.ok("User checked and created if necessary"), response)
        verify(userServiceMock).nicknameExists(nickname)
        verify(userServiceMock).addUser(nickname, id)
    }

    @Test
    fun `checkOrCreateUser should not create user if nickname exists`() {
        val nickname = "testNickname"
        val id = "testId"

        `when`(jwtMock.subject).thenReturn(id)
        `when`(userServiceMock.nicknameExists(nickname)).thenReturn(true)

        val response = userController.checkOrCreateUser(nickname, jwtMock)

        assertEquals(ResponseEntity.ok("User checked and created if necessary"), response)
        verify(userServiceMock).nicknameExists(nickname)
        verify(userServiceMock, never()).addUser(nickname, id)
    }

    @Test
    fun `checkOrCreateUser should return error response on exception`() {
        val nickname = "testNickname"
        val id = "testId"

        `when`(jwtMock.subject).thenReturn(id)
        `when`(userServiceMock.nicknameExists(nickname)).thenThrow(RuntimeException("Test exception"))

        val response = userController.checkOrCreateUser(nickname, jwtMock)

        assertEquals(ResponseEntity.status(500).body("Error during user check: Test exception"), response)
        verify(userServiceMock).nicknameExists(nickname)
    }

    @Test
    fun `nicknameExists should return true if nickname exists`() {
        val nickname = "testNickname"

        `when`(userServiceMock.nicknameExists(nickname)).thenReturn(true)

        val response = userController.nicknameExists(nickname, jwtMock)

        assertEquals(ResponseEntity.ok(true), response)
        verify(userServiceMock).nicknameExists(nickname)
    }

    @Test
    fun `nicknameExists should return false if nickname does not exist`() {
        val nickname = "testNickname"

        `when`(userServiceMock.nicknameExists(nickname)).thenReturn(false)

        val response = userController.nicknameExists(nickname, jwtMock)

        assertEquals(ResponseEntity.ok(false), response)
        verify(userServiceMock).nicknameExists(nickname)
    }
}
