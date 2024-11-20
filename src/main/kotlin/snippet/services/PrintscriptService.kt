package snippet.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import snippet.model.dtos.printscript.*
import java.util.*

@Service
class PrintscriptService(@Value("\${printscript.url}") printscriptUrl: String) {
    private val printscriptApi = WebClient.builder()
        .baseUrl(validateAndFormatUrl(printscriptUrl))
        .build()

    private fun validateAndFormatUrl(url: String): String {
        return if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "http://$url"
        }
    }

    fun formatSnippet(data: FormatFileDto): PrintscriptResponseDTO =
        printscriptApi
            .post()
            .uri("/format")
            .bodyValue(data)
            .retrieve()
            .bodyToMono(PrintscriptResponseDTO::class.java)
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not format correctly")

    fun getFormatRules(userId: String, correlationId: UUID): List<Rule> =
        printscriptApi
            .get()
            .uri("/format/$userId")
            .header("Correlation-id", correlationId.toString())
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Rule>>() {})
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not get rules")

    fun getLintRules(userId: String, correlationId: UUID): List<Rule> =
        printscriptApi
            .get()
            .uri("/lint/$userId")
            .header("Correlation-id", correlationId.toString())
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Rule>>() {})
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not get rules")

    fun changeFormatRules(
        userId: String,
        rules: List<Rule>,
        snippets: List<PrintscriptDataDTO>,
        correlationId: UUID
    ) {
        try {
            val data = ChangeRulesDto(userId, rules, snippets, correlationId)
            printscriptApi
                .put()
                .uri("/redis/format")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Unit::class.java)
                .block()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun changeLintRules(
        userId: String,
        rules: List<Rule>,
        snippets: List<PrintscriptDataDTO>,
        correlationId: UUID
    ) {
        try {
            val data = ChangeRulesDto(userId, rules, snippets, correlationId)
            printscriptApi
                .put()
                .uri("/redis/lint")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Unit::class.java)
                .block()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun runTest(
        snippet: String,
        input: String,
        output: List<String>,
        envVars: String
    ): String =
        printscriptApi
            .post()
            .uri("/test")
            .bodyValue(mapOf("snippet" to snippet, "input" to input, "output" to output, "envVars" to envVars))
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not run test")
}
