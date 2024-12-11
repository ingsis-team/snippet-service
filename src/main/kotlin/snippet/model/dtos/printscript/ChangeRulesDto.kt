package snippet.model.dtos.printscript

import java.util.UUID

data class ChangeRulesDto(
    val userId: String,
    val rules: List<Rule>,
    val snippets: List<PrintscriptDataDTO>,
    val correlationId: UUID,
)
