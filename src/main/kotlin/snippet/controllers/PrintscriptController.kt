package snippet.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import snippet.model.dtos.printscript.ObjectRules
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.snippet.SnippetContext
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.server.CorrelationIdFilter
import snippet.services.SnippetService
import java.util.UUID

@RestController
@RequestMapping("/run")
class PrintscriptController(
    @Autowired private val snippetService: SnippetService,
) {
    private val logger = LoggerFactory.getLogger(PrintscriptController::class.java)

    @PutMapping("/format")
    fun formatOneSnippet(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody snippetContext: SnippetContext,
    ): String {
        val uuid = UUID.randomUUID()
        return snippetService.formatSnippet(jwt.subject, snippetContext.snippetId, snippetContext.language, uuid)
    }

    @GetMapping("/format-rules")
    fun getFormatRules(
        @AuthenticationPrincipal jwt: Jwt,
    ): List<Rule> = snippetService.getFormatRules(jwt.subject, UUID.randomUUID())

    @GetMapping("/lint-rules")
    fun getLintRules(
        @AuthenticationPrincipal jwt: Jwt,
    ): List<Rule> = snippetService.getLintRules(jwt.subject, UUID.randomUUID())

    @PutMapping("/format-rules")
    fun changeFormatRules(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody rules: ObjectRules,
    ): List<Rule> {
        logger.info("Changing format rules")
        snippetService.changeFormatRules(jwt.subject, rules.rules, UUID.randomUUID())
        logger.info("rule changes were completed successfully")

        return rules.rules
    }

    @PutMapping("/lint-rules")
    fun changeLinRules(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody rules: ObjectRules,
    ): List<Rule> {
        snippetService.changeLintRules(jwt.subject, rules.rules, UUID.randomUUID())
        return rules.rules
    }

    @PutMapping("/update-snippet")
    fun updateSnippet(
        @RequestBody snippet: UpdateSnippetDto,
        exchange: ServerWebExchange,
    ) {
        val correlationId = exchange.getAttribute<String>(CorrelationIdFilter.CORRELATION_ID_KEY) ?: "default-correlation-id"
        snippetService.updateFormattedLintedSnippet(snippet.id.toLong(), snippet.content, correlationId)
    }
}
