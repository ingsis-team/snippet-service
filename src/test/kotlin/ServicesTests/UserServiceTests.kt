

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import snippet.model.entities.User
import snippet.repositories.UserRepository
import snippet.services.UserService
import java.util.Optional

class UserServiceTests {
    private lateinit var userService: UserService
    private lateinit var userRepositoryMock: UserRepository

    @BeforeEach
    fun setUp() {
        userRepositoryMock = mock(UserRepository::class.java)
        userService = UserService(userRepositoryMock)
    }

    @Test
    fun `addUser should save and return user`() {
        val nickname = "testNickname"
        val id = "testId"
        val user =
            User().apply {
                this.nickname = nickname
                this.id = id
            }

        `when`(userRepositoryMock.save(any(User::class.java))).thenReturn(user)

        val result = userService.addUser(nickname, id)

        assertEquals(user, result)
        verify(userRepositoryMock).save(any(User::class.java))
    }

    @Test
    fun `nicknameExists should return true if nickname exists`() {
        val nickname = "testNickname"
        `when`(userRepositoryMock.existsById(nickname)).thenReturn(true)

        val result = userService.nicknameExists(nickname)

        assertTrue(result)
        verify(userRepositoryMock).existsById(nickname)
    }

    @Test
    fun `nicknameExists should return false if nickname does not exist`() {
        val nickname = "testNickname"
        `when`(userRepositoryMock.existsById(nickname)).thenReturn(false)

        val result = userService.nicknameExists(nickname)

        assertFalse(result)
        verify(userRepositoryMock).existsById(nickname)
    }

    @Test
    fun `findIdByNickname should return id if nickname exists`() {
        val nickname = "testNickname"
        val id = "testId"
        `when`(userRepositoryMock.findIdByNickname(nickname)).thenReturn(Optional.of(id))

        val result = userService.findIdByNickname(nickname)

        assertEquals(id, result)
        verify(userRepositoryMock).findIdByNickname(nickname)
    }

    @Test
    fun `findIdByNickname should throw exception if nickname does not exist`() {
        val nickname = "testNickname"
        `when`(userRepositoryMock.findIdByNickname(nickname)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            userService.findIdByNickname(nickname)
        }
        verify(userRepositoryMock).findIdByNickname(nickname)
    }
}
