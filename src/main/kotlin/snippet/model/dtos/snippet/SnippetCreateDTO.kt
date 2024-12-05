package snippet.model.dtos.snippet

data class SnippetCreateDto(
    val name: String,
    val language: String,
    val content: String,
    val compliance: String,
    val extension: String,
    val username: String
)