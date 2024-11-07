package snippet.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import snippet.model.entities.Snippet
import snippet.services.SnippetService

@CrossOrigin(origins = ["http://localhost:5173"])  // Add this annotation to allow CORS for frontend origin
@RestController
@RequestMapping("/snippets")
class SnippetController(private val snippetService: SnippetService) {

    @PostMapping("/")
    fun createSnippet(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody snippetData: Snippet
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.createSnippet(snippetData, userId))
    }

    @PutMapping("/{snippetId}")
    fun updateSnippet(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable snippetId: String,
        @RequestBody snippetData: Snippet
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.updateSnippet(snippetId, snippetData, userId))
    }

    @GetMapping("/")
    fun getSnippetsByUser(
        @AuthenticationPrincipal jwt: Jwt,
    ): ResponseEntity<List<Snippet>> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.getSnippetsByUser(userId).toList())
    }

    @GetMapping("/{snippetId}")
    fun getSnippetById(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable snippetId: String
    ): ResponseEntity<Snippet> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.getSnippetById(snippetId, userId))
    }

    @DeleteMapping("/{snippetId}")
    fun deleteSnippetById(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable snippetId: String
    ): ResponseEntity<String> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.deleteSnippetById(snippetId, userId))
    }

    @PostMapping("/{snippetId}/share")
    fun shareSnippet(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable snippetId: String,
        @RequestParam("userToShareWith") otherUserId: String,
    ): ResponseEntity<Unit> {
        val userId = jwt.subject
        return ResponseEntity.ok(snippetService.shareSnippet(snippetId, userId, otherUserId))
    }
}
