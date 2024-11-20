package snippet.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service

class AssetService(@Value("\${asset.url}") assetUrl: String) {

    private val assetServiceApi = WebClient.builder().baseUrl("http://$assetUrl/v1/asset").build()

    fun saveSnippet(key:String, snippet: String, correlationId:String):Boolean{
        return try{
            val responseStatus=
                assetServiceApi
                    .post()
                    .uri("/snippets/{key}",key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("correlationId", correlationId)
                    .bodyValue(snippet)
                    .exchangeToMono{ clientResponse ->
                        if(clientResponse.statusCode() == HttpStatus.CREATED){
                            Mono.just(HttpStatus.CREATED)
                        }else{
                            Mono.just(HttpStatus.BAD_REQUEST)
                        }

                    }
                    .block()
            responseStatus == HttpStatus.CREATED
        }catch(e:Exception){
            println(e.message)
            false
        }
    }


fun getSnippet(key: String): String {
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
    assetServiceApi
        .delete()
        .uri("/snippets/{key}", key)
        .exchangeToMono { clientResponse ->
            if (clientResponse.statusCode() == HttpStatus.NO_CONTENT) {
                Mono.just(HttpStatus.OK)
            } else {
                Mono.just(HttpStatus.BAD_REQUEST)
            }
        }
        .block() ?: throw ChangeSetPersister.NotFoundException()
    return true
}


}
