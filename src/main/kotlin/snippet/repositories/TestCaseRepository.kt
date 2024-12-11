package snippet.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import snippet.model.entities.SnippetTest

@Repository
interface TestCaseRepository : CrudRepository<SnippetTest, Long> {
    fun findBySnippetId(snippetId: Long): MutableList<SnippetTest>
}
