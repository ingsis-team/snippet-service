package snippet.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import snippet.model.entities.Snippet

@Repository
interface SnippetRepository : CrudRepository<Snippet, String>{
    fun getSnippetById(id: String): Snippet
}