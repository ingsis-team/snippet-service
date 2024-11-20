package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
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

    @PostMapping("/")
    fun createSnippet(
        @RequestBody snippetData: SnippetCreateDto
    ): Snippet {
        val correlationId = UUID.randomUUID().toString()
        return snippetService.createSnippet(snippetData, correlationId)
    }


    @GetMapping()
    fun getSnippets(
        @RequestParam userId: String,
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
    ): Page<GetSnippetDto> {
        println("getSnippets")
        return snippetService.getSnippets(userId, pageNumber, pageSize)
    }

    @GetMapping("/byId")
    fun getSnippetById(
        @RequestParam userId: String,
        @RequestParam snippetId: String,
    ): GetSnippetDto = snippetService.getSnippetById(userId, snippetId.toLong())


    @PutMapping()
    fun updateSnippet(
        @RequestParam userId: String,
        @RequestBody updateSnippetDto: UpdateSnippetDto,
    ): GetSnippetDto {
        val correlationId = UUID.randomUUID().toString()
        return snippetService.updateSnippet(userId, updateSnippetDto, correlationId)
    }

    @DeleteMapping("")
    fun deleteSnippet(
        @RequestParam userId: String,
        @RequestParam snippetId: String,
    ) {
        println("user: $userId, snippet: $snippetId")
        snippetService.deleteSnippet(userId, snippetId.toLong())
    }

    @PostMapping("/share")
    fun shareSnippet(
        @RequestParam userId: String,
        @RequestBody snippetFriend: ShareSnippetDTO,
    ): UserResourcePermission =
        snippetService.shareSnippet(userId, snippetFriend.friendId, snippetFriend.snippetId.toLong())

    @GetMapping("users")
    fun getUsers(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
    ): Page<String> = snippetService.getUsers(pageNumber, pageSize)


}