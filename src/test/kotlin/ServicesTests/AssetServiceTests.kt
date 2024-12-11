

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec
import reactor.core.publisher.Mono
import snippet.services.AssetService

class AssetServiceTests {
    private lateinit var assetService: AssetService
    private lateinit var webClientMock: WebClient
    private lateinit var requestBodyUriSpecMock: RequestBodyUriSpec
    private lateinit var requestHeadersUriSpecMock: RequestHeadersUriSpec<*>
    private lateinit var requestHeadersSpecMock: RequestHeadersSpec<*>
    private lateinit var responseSpecMock: ResponseSpec
    private lateinit var clientResponseMock: ClientResponse

    @BeforeEach
    fun setUp() {
        webClientMock = mock(WebClient::class.java)
        requestBodyUriSpecMock = mock(RequestBodyUriSpec::class.java)
        requestHeadersUriSpecMock = mock(RequestHeadersUriSpec::class.java)
        requestHeadersSpecMock = mock(RequestHeadersSpec::class.java)
        responseSpecMock = mock(ResponseSpec::class.java)
        clientResponseMock = mock(ClientResponse::class.java)

        `when`(webClientMock.put()).thenReturn(requestBodyUriSpecMock)
        `when`(webClientMock.get()).thenReturn(requestHeadersUriSpecMock)
        `when`(webClientMock.delete()).thenReturn(requestHeadersUriSpecMock)

        assetService = AssetService("localhost:8080")
        assetService.assetServiceApi = webClientMock
    }

    @Test
    fun `saveSnippet should return true when snippet is saved successfully`() {
        `when`(requestBodyUriSpecMock.uri(anyString(), anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.header(anyString(), anyString())).thenReturn(requestBodyUriSpecMock)
        `when`(requestBodyUriSpecMock.bodyValue(anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.exchangeToMono<Any>(any())).thenReturn(Mono.just(HttpStatus.CREATED))

        val result = assetService.saveSnippet("key", "snippet", "correlationId")

        assertTrue(result)
    }

    @Test
    fun `getSnippet should return snippet when found`() {
        `when`(requestHeadersUriSpecMock.uri(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(String::class.java)).thenReturn(Mono.just("snippet"))

        val result = assetService.getSnippet("key")

        assertEquals("snippet", result)
    }

    @Test
    fun `deleteSnippet should return true when snippet is deleted successfully`() {
        `when`(requestHeadersUriSpecMock.uri(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.exchangeToMono<Any>(any())).thenReturn(Mono.just(clientResponseMock))
        `when`(clientResponseMock.statusCode()).thenReturn(HttpStatus.NO_CONTENT)

        val result = assetService.deleteSnippet("key")

        assertFalse(result)
    }
}
