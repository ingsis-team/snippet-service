import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.server.ServerWebExchange
import snippet.controllers.PrintscriptController
import snippet.services.SnippetService

class PrintScriptControllerTests {
    private lateinit var snippetService: SnippetService
    private lateinit var printscriptController: PrintscriptController
    private lateinit var jwtMock: Jwt
    private lateinit var exchangeMock: ServerWebExchange

    @BeforeEach
    fun setUp() {
        snippetService = mock(SnippetService::class.java)
        printscriptController = PrintscriptController(snippetService)
        jwtMock = mock(Jwt::class.java)
        exchangeMock = mock(ServerWebExchange::class.java)
    }
}
