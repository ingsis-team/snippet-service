package snippet.repositories

import org.springframework.data.repository.CrudRepository
import snippet.model.entities.Snippet

interface SnippetRepository : CrudRepository<Snippet, Long>{
    fun addSnippet(snippet: Snippet);
    fun updateSnippetById(id: String);
    fun getAllSnippets() : List<Snippet>;
    fun getSnippetById(id: String) : Snippet;

}