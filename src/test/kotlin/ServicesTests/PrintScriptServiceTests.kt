import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec
import reactor.core.publisher.Mono
import snippet.model.dtos.printscript.FormatFileDto
import snippet.model.dtos.printscript.PrintscriptDataDTO
import snippet.model.dtos.printscript.PrintscriptResponseDTO
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.printscript.ValidationResult
import snippet.services.PrintscriptService
import java.util.UUID

class PrintScriptServiceTests {
    private lateinit var printscriptService: PrintscriptService
    private lateinit var webClientMock: WebClient
    private lateinit var requestBodyUriSpecMock: RequestBodyUriSpec
    private lateinit var requestHeadersUriSpecMock: RequestHeadersUriSpec<*>
    private lateinit var requestHeadersSpecMock: RequestHeadersSpec<*>
    private lateinit var responseSpecMock: ResponseSpec

    @BeforeEach
    fun setUp() {
        webClientMock = mock(WebClient::class.java)
        requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec::class.java)
        requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec::class.java)
        requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec::class.java)
        responseSpecMock = mock(WebClient.ResponseSpec::class.java)

        `when`(webClientMock.post()).thenReturn(requestBodyUriSpecMock)
        `when`(webClientMock.get()).thenReturn(requestHeadersUriSpecMock)
        `when`(webClientMock.put()).thenReturn(requestBodyUriSpecMock)

        printscriptService = PrintscriptService("localhost:8080")
        printscriptService.printscriptApi = webClientMock
    }

    @Test
    fun `validate should return ValidationResult`() {
        val validationResult = ValidationResult(true, "rule", 1, 1)
        `when`(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(ValidationResult::class.java)).thenReturn(Mono.just(validationResult))

        val result = printscriptService.validate("content")

        assertEquals(validationResult, result)
    }

    @Test
    fun `formatSnippet should return PrintscriptResponseDTO`() {
        val responseDTO = PrintscriptResponseDTO("correlationId", "snippetId", "snippet")
        `when`(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(PrintscriptResponseDTO::class.java)).thenReturn(Mono.just(responseDTO))

        val result =
            printscriptService.formatSnippet(
                FormatFileDto(UUID.randomUUID(), "snippetId", "printscript", "version", "input", "userId"),
            )

        assertEquals(responseDTO, result)
    }

    @Test
    fun `getFormatRules should return list of rules`() {
        val rules = listOf(Rule("1", "rule1", true, 1))
        `when`(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.header(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(any<ParameterizedTypeReference<List<Rule>>>())).thenReturn(Mono.just(rules))

        val result = printscriptService.getFormatRules("userId", UUID.randomUUID())

        assertEquals(rules, result)
    }

    @Test
    fun `getLintRules should return list of rules`() {
        val rules = listOf(Rule("1", "rule1", true, 1))
        `when`(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.header(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(any<ParameterizedTypeReference<List<Rule>>>())).thenReturn(Mono.just(rules))

        val result = printscriptService.getLintRules("userId", UUID.randomUUID())

        assertEquals(rules, result)
    }

    @Test
    fun `changeFormatRules should not throw exception when successful`() {
        `when`(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(Unit::class.java)).thenReturn(Mono.just(Unit))

        assertDoesNotThrow {
            printscriptService.changeFormatRules(
                "userId",
                listOf(Rule("1", "rule1", true, 1)),
                listOf(PrintscriptDataDTO(UUID.randomUUID(), "snippetId", "printscript", "version", "input")),
                UUID.randomUUID(),
            )
        }
    }

    @Test
    fun `changeLintRules should not throw exception when successful`() {
        `when`(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(Unit::class.java)).thenReturn(Mono.just(Unit))

        assertDoesNotThrow {
            printscriptService.changeFormatRules(
                "userId",
                listOf(Rule("1", "rule1", true, 1)),
                listOf(PrintscriptDataDTO(UUID.randomUUID(), "snippetId", "printscript", "version", "input")),
                UUID.randomUUID(),
            )
        }
    }

    @Test
    fun `runTest should return result string`() {
        val resultString = "testResult"
        `when`(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(String::class.java)).thenReturn(Mono.just(resultString))

        val result = printscriptService.runTest("snippet", "input", listOf("output"), "envVars")

        assertEquals(resultString, result)
    }
}
