package snippet.model.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import org.jetbrains.annotations.NotNull
import snippet.model.dtos.snippet.SnippetCreateDto

@Entity
class Snippet {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippet_seq")
    @SequenceGenerator(name = "snippet_seq", sequenceName = "snippet_sequence", allocationSize = 1)
    var id: Long = 0

    @Column
    var name: String = ""

    @Column
    var compliance: String = ""

    @Column
    var language: String = ""

    @Column
    var extension: String = ""

    @Column
    var author: String = " "

    @Column
    var username: String = ""

    @Column
    var content: String = " "

    companion object {
        fun from(
            snippetDto: SnippetCreateDto,
            authorId: String,
            username: String,
        ): Snippet {
            val snippet = Snippet()
            snippet.name = snippetDto.name
            snippet.compliance = snippetDto.compliance
            snippet.language = snippetDto.language
            snippet.extension = snippetDto.extension
            snippet.author = authorId
            snippet.content = snippetDto.content
            snippet.username = username
            return snippet
        }
    }
}
