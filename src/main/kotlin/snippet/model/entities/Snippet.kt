package snippet.model.entities

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import snippet.model.dtos.snippet.SnippetCreateDto

@Entity
class Snippet {
    @Id()
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @Column
    var name: String = ""

    @Column
    var compliance: String = ""

    @Column
    var language: String = ""

    @Column
    var extension: String = ""

    companion object {
        fun from(snippetDto: SnippetCreateDto): Snippet {
            val snippet = Snippet()
            snippet.name = snippetDto.name
            snippet.compliance = snippetDto.compliance
            snippet.language = snippetDto.language
            snippet.extension = snippetDto.extension
            return snippet
        }
    }
}