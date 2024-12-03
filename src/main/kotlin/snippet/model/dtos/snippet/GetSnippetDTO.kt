package snippet.model.dtos.snippet

import snippet.model.entities.Snippet

data class GetSnippetDto(
    val id: String,
    val name: String,
    val language: String,
    val content: String,
    val compliance: String,
    val extension: String,
    val author: String,
) {
    companion object {
        fun from(
            snippet: Snippet,
            content: String,
        ): GetSnippetDto {
            return GetSnippetDto(
                snippet.id.toString(),
                snippet.name,
                snippet.language,
                content,
                snippet.compliance,
                snippet.extension,
                snippet.author,
            )
        }
    }
}