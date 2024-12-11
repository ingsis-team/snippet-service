package snippet.model.dtos.printscript

import java.util.UUID

data class PrintscriptDataDTO(
    val correlationId: UUID,
    val snippetId: String,
    val language: String,
    val version: String,
    val input: String,
)
