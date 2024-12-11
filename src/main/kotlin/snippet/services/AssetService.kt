package snippet.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class AssetService(
    @Value("\${ASSET_URL}") assetUrl: String,
) {
    private val logger = LoggerFactory.getLogger(AssetService::class.java)
    private val assetServiceApi = WebClient.builder().baseUrl("http://$assetUrl/v1/asset").build()

    fun saveSnippet(
        key: String,
        snippet: String,
        correlationId: String,
    ): Boolean {
        logger.info("Attempting to save snippet with key: $key and correlationId: $correlationId")
        return try {
            val responseStatus =
                assetServiceApi
                    .put()
                    .uri("/snippets/{key}", key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("correlationId", correlationId)
                    .bodyValue(snippet)
                    .exchangeToMono { clientResponse ->
                        logger.info("Received response status: ${clientResponse.statusCode()}")

                        if (clientResponse.statusCode() == HttpStatus.CREATED) {
                            Mono.just(HttpStatus.CREATED)
                        } else {
                            Mono.just(HttpStatus.BAD_REQUEST)
                        }
                    }
                    .block()
            responseStatus == HttpStatus.CREATED
        } catch (e: Exception) {
            logger.error("Exception occurred while saving snippet: ${e.message}", e)
            false
        }
    }

    fun getSnippet(key: String): String {
        logger.info("Attempting to get snippet with key: $key")

        val response =
            assetServiceApi
                .get()
                .uri("/snippets/{key}", key)
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found")
        return response
    }

    fun deleteSnippet(key: String): Boolean {
        logger.info("Attempting to delete snippet with key: $key")
        return try {
            val responseStatus =
                assetServiceApi
                    .delete()
                    .uri("/snippets/{key}", key)
                    .exchangeToMono { clientResponse ->
                        logger.info("Received response status: ${clientResponse.statusCode()}")
                        if (clientResponse.statusCode() == HttpStatus.NO_CONTENT) {
                            Mono.just(HttpStatus.OK)
                        } else {
                            Mono.just(HttpStatus.BAD_REQUEST)
                        }
                    }
                    .block() ?: throw ChangeSetPersister.NotFoundException()
            responseStatus == HttpStatus.OK
        } catch (e: Exception) {
            logger.error("Exception occurred while deleting snippet: ${e.message}", e)
            false
        }
    }
}
