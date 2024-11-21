package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.ShareSnippetDTO
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.model.entities.Snippet
import snippet.services.SnippetService
import java.util.*

@RestController
@RequestMapping("/snippets")
class SnippetController(
    @Autowired val snippetService: SnippetService) {

    @PostMapping()
    fun createSnippet(
        @RequestBody snippetData: SnippetCreateDto,
        @AuthenticationPrincipal jwt: Jwt
    ): Snippet {
        val correlationId = UUID.randomUUID().toString()
        val userId = jwt.subject
        val snippetDataWithAuthor = snippetData.copy(authorId = userId)
        return snippetService.createSnippet(snippetDataWithAuthor, correlationId)
    }

    @GetMapping()
    fun getSnippets(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
        @AuthenticationPrincipal jwt: Jwt
    ): Page<GetSnippetDto> {
        println("getSnippets")
        val userId = jwt.subject
        return snippetService.getSnippets(userId, pageNumber, pageSize)
    }

    @GetMapping("/byId")
    fun getSnippetById(
        @RequestParam snippetId: String,
        @AuthenticationPrincipal jwt: Jwt
    ): GetSnippetDto {
        val userId = jwt.subject
        return snippetService.getSnippetById(userId, snippetId.toLong())
    }


    @PutMapping()
    fun updateSnippet(
        @RequestBody updateSnippetDto: UpdateSnippetDto,
        @AuthenticationPrincipal jwt: Jwt
    ): GetSnippetDto {
        val userId = jwt.subject
        val correlationId = UUID.randomUUID().toString()
        return snippetService.updateSnippet(userId, updateSnippetDto, correlationId)
    }

    @DeleteMapping("")
    fun deleteSnippet(
        @RequestParam snippetId: String,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        val userId = jwt.subject
        println("user: $userId, snippet: $snippetId")
        snippetService.deleteSnippet(userId, snippetId.toLong())
    }

    @PostMapping("/share")
    fun shareSnippet(
        @RequestBody snippetFriend: ShareSnippetDTO,
        @AuthenticationPrincipal jwt: Jwt
    ): UserResourcePermission {
        val userId = jwt.subject
        return snippetService.shareSnippet(userId, snippetFriend.friendId, snippetFriend.snippetId.toLong())
    }

    @GetMapping("users")
    fun getUsers(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
    ): Page<String> = snippetService.getUsers(pageNumber, pageSize)

}