package snippet.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.ShareSnippetDTO
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.services.SnippetService
import java.util.*

@RestController
@RequestMapping("/snippets")
@CrossOrigin(origins = ["http://localhost:5173"], allowedHeaders = ["Authorization", "Content-Type", "ngrok-skip-browser-warning"])

class SnippetController(
    @Autowired val snippetService: SnippetService
) {
    private val logger = LoggerFactory.getLogger(SnippetController::class.java)

    @PostMapping
    fun createSnippet(
        @RequestBody snippetData: SnippetCreateDto,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Any> {
        logger.info("POST /snippets request received. User: ${jwt.subject}")
        val userId = jwt.subject ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token")
        val correlationId = UUID.randomUUID().toString()
        val snippetDataWithAuthor = snippetData.copy(authorId = userId)
        return ResponseEntity.ok(snippetService.createSnippet(snippetDataWithAuthor, correlationId))
    }

    @GetMapping
    fun getSnippets(
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @AuthenticationPrincipal jwt: Jwt?
    ): Page<GetSnippetDto>? {
        logger.info("GET /snippets request received. User: ${jwt?.subject}")
        val userId = jwt?.subject ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token")
        return snippetService.getSnippets(userId, pageNumber, pageSize)
    }

    @GetMapping("/byId")
    fun getSnippetById(
        @RequestParam snippetId: String,
        @AuthenticationPrincipal jwt: Jwt
    ): GetSnippetDto {
        logger.info("GET /snippets/byId request received. User: ${jwt.subject}, SnippetId: $snippetId")
        val userId = jwt.subject
        return snippetService.getSnippetById(userId, snippetId.toLong())
    }


    @PutMapping()
    fun updateSnippet(
        @RequestBody updateSnippetDto: UpdateSnippetDto,
        @AuthenticationPrincipal jwt: Jwt
    ): GetSnippetDto {
        logger.info("PUT /snippets request received. User: ${jwt.subject}")
        val userId = jwt.subject
        val correlationId = UUID.randomUUID().toString()
        return snippetService.updateSnippet(userId, updateSnippetDto, correlationId)
    }

    @DeleteMapping("")
    fun deleteSnippet(
        @RequestParam snippetId: String,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        logger.info("DELETE /snippets request received. User: ${jwt.subject}, SnippetId: $snippetId")
        val userId = jwt.subject
        snippetService.deleteSnippet(userId, snippetId.toLong())
    }

    @PostMapping("/share")
    fun shareSnippet(
        @RequestBody snippetFriend: ShareSnippetDTO,
        @AuthenticationPrincipal jwt: Jwt
    ): UserResourcePermission {
        logger.info("POST /snippets/share request received. User: ${jwt.subject}")
        val userId = jwt.subject
        return snippetService.shareSnippet(userId, snippetFriend.friendId, snippetFriend.snippetId.toLong())
    }

    @GetMapping("users")
    fun getUsers(
        @RequestParam pageNumber: Int,
        @RequestParam pageSize: Int,
    ): Page<String> {
        logger.info("GET /snippets/users request received.")
        return snippetService.getUsers(pageNumber, pageSize)
    }
}
