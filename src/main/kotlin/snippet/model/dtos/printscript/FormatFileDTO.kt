package snippet.model.dtos.printscript

import java.util.*

data class FormatFileDto(
    val correlationId: UUID,
    val snippetId: String,
    val language: String,
    val version: String,
    val input: String,
    val userId: String,
)