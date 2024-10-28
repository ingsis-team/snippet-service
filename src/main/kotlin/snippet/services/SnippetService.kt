package snippet.services

import org.springframework.stereotype.Service
import snippet.model.entities.Snippet
import snippet.repositories.SnippetRepository

@Service
class SnippetService(private val snippetRepository: SnippetRepository){

    fun addSnippet(snippet: Snippet){

    }

    fun updateSnippet(id: String, snippet: Snippet){

    }

    fun getSnippetsByUser(token: String){

    }

    fun getSnippetById(id: String){

    }

    fun shareSnippetWith(snippetId: String, myId:String, otherUserId: String){

    }
}