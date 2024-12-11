import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import snippet.exceptions.GlobalExceptionHandler
import snippet.exceptions.InvalidSnippetException
import snippet.exceptions.PermissionDeniedException
import snippet.exceptions.SnippetNotFoundException

class ExceptionsTests {
    private lateinit var exceptionHandlers: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        exceptionHandlers = GlobalExceptionHandler()
    }

    @Test
    fun `handle AuthenticationCredentialsNotFoundException`() {
        val exception = AuthenticationCredentialsNotFoundException("Authentication credentials not found")
        val response: ResponseEntity<String> = exceptionHandlers.handleAuthenticationException(exception)
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("Authentication failed: Authentication credentials not found", response.body)
    }

    @Test
    fun `handle AccessDeniedException`() {
        val exception = AccessDeniedException("Access is denied")
        val response: ResponseEntity<String> = exceptionHandlers.handleAccessDeniedException(exception)
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals("Access denied: Access is denied", response.body)
    }

    @Test
    fun `handle SnippetNotFoundException`() {
        val exception = SnippetNotFoundException("Snippet not found")
        val response: ResponseEntity<String> = exceptionHandlers.handleSnippetNotFound(exception)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Snippet not found", response.body)
    }

    @Test
    fun `handle PermissionDeniedException`() {
        val exception = PermissionDeniedException("Permission denied")
        val response: ResponseEntity<String> = exceptionHandlers.handlePermissionDenied(exception)
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        assertEquals("Permission denied", response.body)
    }

    @Test
    fun `handle IllegalArgumentException`() {
        val exception = IllegalArgumentException("Invalid argument")
        val response: ResponseEntity<String> = exceptionHandlers.handleIllegalArgument(exception)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid argument", response.body)
    }

    @Test
    fun `handle GenericException`() {
        val exception = Exception("Internal server error")
        val response: ResponseEntity<String> = exceptionHandlers.handleGenericException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("An error occurred: Internal server error", response.body)
    }

    @Test
    fun `handle InvalidSnippetException`() {
        val exception = InvalidSnippetException("Some rule", 1, 1)
        val response: ResponseEntity<String> = exceptionHandlers.handleGenericException(exception)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("An error occurred: Invalid snippet: Some rule at line 1, column 1", response.body)
    }
}
