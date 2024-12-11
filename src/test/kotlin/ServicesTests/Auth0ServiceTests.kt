

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.contains
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import snippet.services.Auth0Service

class Auth0ServiceTests {
    private lateinit var auth0Service: Auth0Service
    private lateinit var restTemplateMock: RestTemplate

    @BeforeEach
    fun setUp() {
        restTemplateMock = mock(RestTemplate::class.java)
        auth0Service = Auth0Service("http://auth0.com/", "clientId", "clientSecret")
        auth0Service.restTemplate = restTemplateMock
    }

    @Test
    fun `getUsers should return list of users`() {
        val accessToken = "test_token"
        val users = listOf(mapOf("id" to "user1"), mapOf("id" to "user2"))
        val response = ResponseEntity(users, HttpStatus.OK)
        `when`(restTemplateMock.postForEntity(anyString(), any(HttpEntity::class.java), eq(Map::class.java)))
            .thenReturn(ResponseEntity(mapOf("access_token" to accessToken), HttpStatus.OK))
        `when`(
            restTemplateMock.exchange(
                contains("page=0"),
                eq(HttpMethod.GET),
                any(HttpEntity::class.java),
                any<ParameterizedTypeReference<List<Map<String, String>>>>(),
            ),
        )
            .thenReturn(response)
    }
}
