package snippet.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class Auth0Service(
    @Value("\${AUTH0_SERVER_URI}") private val authServerUri: String,
    @Value("\${AUTH0_CLIENT_ID}") private val clientId: String,
    @Value("\${AUTH0_CLIENT_SECRET}") private val clientSecret: String,
) {
    private val logger = LoggerFactory.getLogger(Auth0Service::class.java)
    private val restTemplate = RestTemplate()

    fun getAccessToken(): String {
        val restTemplate = RestTemplate()
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }
        val body =
            mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "audience" to "${authServerUri}api/v2/",
                "grant_type" to "client_credentials",
            )

        val response =
            restTemplate.postForEntity(
                "${authServerUri}oauth/token",
                HttpEntity(body, headers),
                Map::class.java,
            )

        val token = response.body?.get("access_token") as? String
        logger.info("Access token obtained successfully")
        return token ?: throw RuntimeException("Failed to get access token")
    }

    fun getUsers(
        page: Int,
        perPage: Int,
    ): List<Map<String, Any>> {
        val accessToken = getAccessToken()
        val restTemplate = RestTemplate()
        val headers =
            HttpHeaders().apply {
                set("Authorization", "Bearer $accessToken")
            }

        val response =
            restTemplate.exchange(
                "${authServerUri}api/v2/users?page=$page&per_page=$perPage",
                HttpMethod.GET,
                HttpEntity<Any>(headers),
                List::class.java,
            )

        return response.body as List<Map<String, Any>>? ?: emptyList()
    }

    fun getAllUsers(): List<Map<String, Any>> {
        val accessToken = getAccessToken()
        val headers =
            org.springframework.http.HttpHeaders().apply {
                set("Authorization", "Bearer $accessToken")
            }

        val users = mutableListOf<Map<String, Any>>()
        var page = 0
        val perPage = 100
        var hasMore = true

        while (hasMore) {
            val response: ResponseEntity<List<Map<String, Any>>> =
                restTemplate.exchange(
                    "${authServerUri}api/v2/users?page=$page&per_page=$perPage",
                    HttpMethod.GET,
                    HttpEntity<Any>(headers),
                    object : ParameterizedTypeReference<List<Map<String, Any>>>() {},
                )
            val currentUsers = response.body ?: emptyList()
            users.addAll(currentUsers)
            hasMore = currentUsers.size == perPage // Si recibimos menos usuarios que `perPage`, significa que no hay más usuarios.
            page++
        }
        logger.info("Total users fetched: ${users.size}")
        return users
    }
}
