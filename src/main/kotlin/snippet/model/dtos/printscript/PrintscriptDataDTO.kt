package snippet.model.dtos.printscript

import java.util.*

data class PrintscriptDataDTO(val correlationId: UUID,
                              val snippetId: String,
                              val language: String,
                              val version: String,
                              val input: String,)
