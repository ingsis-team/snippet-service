package snippet.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import snippet.model.dtos.printscript.ChangeRulesDto
import snippet.model.dtos.printscript.FormatFileDto
import snippet.model.dtos.printscript.PrintscriptDataDTO
import snippet.model.dtos.printscript.PrintscriptResponseDTO
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.printscript.ValidationResult
import java.util.UUID

@Service
class PrintscriptService(
    @Value("\${printscript.url}") printscriptUrl: String,
) {
    private val logger = LoggerFactory.getLogger(PrintscriptService::class.java)

    private val printscriptApi =
        WebClient.builder()
            .baseUrl(validateAndFormatUrl(printscriptUrl))
            .build()

    private fun validateAndFormatUrl(url: String): String {
        return if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "http://$url"
        }
    }

    fun validate(content: String): ValidationResult =
        printscriptApi
            .post()
            .uri("/validate")
            .bodyValue(content)
            .retrieve()
            .bodyToMono(ValidationResult::class.java)
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not validate snippet")

    fun formatSnippet(data: FormatFileDto): PrintscriptResponseDTO =
        printscriptApi
            .post()
            .uri("/format")
            .bodyValue(data)
            .retrieve()
            .bodyToMono(PrintscriptResponseDTO::class.java)
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not format correctly")

    fun getFormatRules(
        userId: String,
        correlationId: UUID,
    ): List<Rule> =
        printscriptApi
            .get()
            .uri("/format/$userId")
            .header("Correlation-id", correlationId.toString())
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Rule>>() {})
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not get rules")

    fun getLintRules(
        userId: String,
        correlationId: UUID,
    ): List<Rule> =
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
        correlationId: UUID,
    ) {
        logger.info("Starting changeFormatRules with userId: $userId, correlationId: $correlationId")
        logger.debug("Rules: $rules")
        logger.debug("Snippets: $snippets")

        try {
            val data = ChangeRulesDto(userId, rules, snippets, correlationId)
            logger.info("Sending request to /redis/format with data: $data")

            printscriptApi
                .put()
                .uri("/redis/format")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Unit::class.java)
                .block()

            logger.info("Successfully changed format rules for userId: $userId")
        } catch (e: Exception) {
            logger.error("Error changing format rules for userId: $userId, message: ${e.message}")
        }
    }

    fun changeLintRules(
        userId: String,
        rules: List<Rule>,
        snippets: List<PrintscriptDataDTO>,
        correlationId: UUID,
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
        envVars: String,
    ): String =
        printscriptApi
            .post()
            .uri("/test")
            .bodyValue(mapOf("snippet" to snippet, "input" to input, "output" to output, "envVars" to envVars))
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Could not run test")
}
