package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import snippet.model.dtos.printscript.ObjectRules
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.snippet.SnippetContext
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.server.CorrelationIdFilter
import snippet.services.SnippetService
import java.util.*

@RestController
@RequestMapping
class PrintscriptController(
    @Autowired private val snippetService: SnippetService,
) {

    @PutMapping("/format")
    fun formatOneSnippet(
        @RequestParam userId: String,
        @RequestBody snippetContext: SnippetContext,
    ): String {
        val uuid = UUID.randomUUID()
        return snippetService.formatSnippet(userId, snippetContext.snippetId, snippetContext.language, uuid)
    }

    @GetMapping("/format-rules")
    fun getFormatRules(
        @RequestParam userId: String,
    ): List<Rule> = snippetService.getFormatRules(userId, UUID.randomUUID())

    @GetMapping("/lint-rules")
    fun getLintRules(
        @RequestParam userId: String,
    ): List<Rule> = snippetService.getLintRules(userId, UUID.randomUUID())

    @PutMapping("/format-rules")
    fun changeFormatRules(
        @RequestParam userId: String,
        @RequestBody rules: ObjectRules,
    ): List<Rule> {
        println("Changing format rules")
        snippetService.changeFormatRules(userId, rules.rules, UUID.randomUUID())
        return rules.rules
    }

    @PutMapping("/lint-rules")
    fun changeLinRules(
        @RequestParam userId: String,
        @RequestBody rules: ObjectRules,
    ): List<Rule> {
        snippetService.changeFormatRules(userId, rules.rules, UUID.randomUUID())
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