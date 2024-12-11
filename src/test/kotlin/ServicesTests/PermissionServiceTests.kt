

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec
import reactor.core.publisher.Mono
import snippet.services.PermissionService

class PermissionServiceTests {
    private lateinit var permissionService: PermissionService
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
        `when`(webClientMock.delete()).thenReturn(requestHeadersUriSpecMock)

        permissionService = PermissionService("localhost:8080")
        permissionService.permissionApi = webClientMock
    }

    @Test
    fun `deleteResourcePermissions should not throw exception when successful`() {
        `when`(requestHeadersUriSpecMock.uri(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.cookie(anyString(), anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(String::class.java)).thenReturn(Mono.just(""))

        assertDoesNotThrow {
            permissionService.deleteResourcePermissions("userId", "resourceId")
        }
    }

    @Test
    fun `getUsers should return list of users`() {
        val users = listOf("user1", "user2")
        `when`(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock)
        `when`(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock)
        `when`(responseSpecMock.bodyToMono(any<ParameterizedTypeReference<List<String>>>())).thenReturn(Mono.just(users))

        val result = permissionService.getUsers()

        assertEquals(users, result)
    }
}
