package snippet.repositories

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import snippet.model.entities.Snippet

@Repository
interface SnippetRepositoryPage : PagingAndSortingRepository<Snippet, Long>
