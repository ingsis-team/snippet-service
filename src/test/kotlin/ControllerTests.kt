import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.server.ServerWebExchange
import snippet.controllers.PrintscriptController
import snippet.model.dtos.printscript.ObjectRules
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.services.SnippetService

class ControllerTests {
    private lateinit var snippetService: SnippetService
    private lateinit var controller: PrintscriptController

    @BeforeEach
    fun setUp() {
        snippetService = Mockito.mock(SnippetService::class.java)
        controller = PrintscriptController(snippetService)
    }
    /*
    @Test
    fun `formatOneSnippet should return formatted snippet`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val snippetContext = SnippetContext("snippetId", "language")
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.formatSnippet(any(), any(), any(), any())).thenReturn("formattedSnippet")

        val result = controller.formatOneSnippet(jwt, snippetContext)

        assertEquals("formattedSnippet", result)
    }

    @Test
    fun `getFormatRules should return format rules`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val rules = listOf(Rule("rule1", "description1", true, 1))
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.getFormatRules(any(), any())).thenReturn(rules)

        val result = controller.getFormatRules(jwt)

        assertEquals(rules, result)
    }

    @Test
    fun `getLintRules should return lint rules`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val rules = listOf(Rule("rule1", "description1", true, 1))
        Mockito.`when`(jwt.subject).thenReturn("user")
        Mockito.`when`(snippetService.getLintRules(any(), any())).thenReturn(rules)

        val result = controller.getLintRules(jwt)

        assertEquals(rules, result)
    }

     */

    @Test
    fun `changeFormatRules should return updated rules`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val rules = ObjectRules(listOf(Rule("rule1", "description1", true, 1)))
        Mockito.`when`(jwt.subject).thenReturn("user")

        val result = controller.changeFormatRules(jwt, rules)

        assertEquals(rules.rules, result)
    }
/*
    @Test
    fun `changeLinRules should return updated rules`() {
        val jwt = Mockito.mock(Jwt::class.java)
        val rules = ObjectRules(listOf(Rule("rule1", "description1", true, 1)))
        Mockito.`when`(jwt.subject).thenReturn("user")

        val result = controller.changeLinRules(jwt, rules)

        assertEquals(rules.rules, result)
    }

 */

    @Test
    fun `updateSnippet should call updateFormattedLintedSnippet`() {
        val snippet = UpdateSnippetDto("1", "content")
        val exchange = Mockito.mock(ServerWebExchange::class.java)
        Mockito.`when`(exchange.getAttribute<String>(any())).thenReturn("correlationId")

        controller.updateSnippet(snippet, exchange)

        Mockito.verify(snippetService).updateFormattedLintedSnippet(1L, "content", "correlationId")
    }
}
