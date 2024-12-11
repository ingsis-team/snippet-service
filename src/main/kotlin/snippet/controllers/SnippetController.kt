package snippet.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.ShareSnippetDTO
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.security.JwtUtil
import snippet.services.Auth0Service
import snippet.services.SnippetService
import java.util.UUID

@RestController
@RequestMapping("/snippets")
@CrossOrigin(origins = ["http://localhost:5173"], allowedHeaders = ["Authorization", "Content-Type", "ngrok-skip-browser-warning"])
class SnippetController
    @Autowired
    constructor(
        private val snippetService: SnippetService,
        private val auth0Service: Auth0Service,
        private val jwtUtil: JwtUtil,
    ) {
        private val logger = LoggerFactory.getLogger(SnippetController::class.java)

        @PostMapping()
        fun createSnippet(
            @RequestBody snippetData: SnippetCreateDto,
            @AuthenticationPrincipal jwt: Jwt,
        ): ResponseEntity<Any> {
            return try {
                logger.info("POST /snippets request received. User: ${jwt.subject}")
                val correlationId = UUID.randomUUID().toString()
                val snippet = snippetService.createSnippet(snippetData, correlationId, jwt.subject, snippetData.username)
                ResponseEntity.ok(snippet)
            } catch (e: ResponseStatusException) {
                ResponseEntity.status(e.statusCode).body(mapOf("error" to e.reason))
            } catch (e: Exception) {
                logger.error("Unexpected error during snippet creation: ${e.message}", e)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to e.message))
            }
        }

        @GetMapping()
        fun getSnippets(
            @RequestParam pageNumber: Int,
            @RequestParam pageSize: Int,
            @AuthenticationPrincipal jwt: Jwt?,
        ): Page<GetSnippetDto>? {
            logger.info("GET /snippets request received. User: ${jwt?.subject}")
            val userId = jwt?.subject
            return userId?.let { snippetService.getSnippets(it, pageNumber, pageSize) }
        }

        @GetMapping("/byId")
        fun getSnippetById(
            @RequestParam snippetId: String,
            @AuthenticationPrincipal jwt: Jwt,
        ): GetSnippetDto {
            logger.info("GET /snippets/byId request received. User: ${jwt.subject}, SnippetId: $snippetId")
            val userId = jwt.subject
            return snippetService.getSnippetById(userId, snippetId.toLong())
        }

        @PutMapping()
        fun updateSnippet(
            @RequestBody updateSnippetDto: UpdateSnippetDto,
            @AuthenticationPrincipal jwt: Jwt,
        ): ResponseEntity<Any> {
            logger.info("JSON recibido: id=${updateSnippetDto.id}, content=${updateSnippetDto.content}")
            logger.info("PUT /snippets request received. User: ${jwt.subject}")
            return try {
                logger.info("Request body: $updateSnippetDto")
                val correlationId = UUID.randomUUID().toString()
                val updatedSnippet = snippetService.updateSnippet(jwt.subject, updateSnippetDto, correlationId)
                ResponseEntity.ok(updatedSnippet)
            } catch (e: Exception) {
                logger.error("Error updating snippet: ${e.message}")
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Error updating snippet: ${e.message}"))
            }
        }

        @DeleteMapping("")
        fun deleteSnippet(
            @RequestParam snippetId: String,
            @AuthenticationPrincipal jwt: Jwt,
        ) {
            logger.info("DELETE /snippets request received. User: ${jwt.subject}, SnippetId: $snippetId")
            val userId = jwt.subject
            snippetService.deleteSnippet(userId, snippetId.toLong())
        }

        @PostMapping("/share")
        fun shareSnippet(
            @RequestBody snippetFriend: ShareSnippetDTO,
            @AuthenticationPrincipal jwt: Jwt,
        ): ResponseEntity<UserResourcePermission> {
            logger.info("Solicitud recibida para compartir snippet: snippetId=${snippetFriend.snippetId}, friendUsername=${snippetFriend.friendUsername}")
            return try {
                val userId = jwt.subject ?: return ResponseEntity.status(401).build()
                val response = snippetService.shareSnippet(userId, snippetFriend.friendUsername, snippetFriend.snippetId.toLong())
                ResponseEntity.ok(response)
            } catch (e: ResponseStatusException) {
                logger.error("Error al compartir snippet: ${e.reason}")
                ResponseEntity.status(e.statusCode).body(null)
            } catch (e: Exception) {
                logger.error("Error inesperado al compartir snippet", e)
                ResponseEntity.status(500).body(null)
            }
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
