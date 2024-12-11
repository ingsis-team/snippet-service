package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import snippet.services.UserService

@RestController
@RequestMapping("/users")
class UserController
    @Autowired
    constructor(
        private val userService: UserService,
        ) {

        @PostMapping("/check-or-create")
        fun checkOrCreateUser(
            @RequestParam nickname: String,
            @AuthenticationPrincipal jwt: Jwt,
            ): ResponseEntity<String> {
            return try {
                val id = jwt.subject
                if (!userService.nicknameExists(nickname)) {
                    userService.addUser(nickname, id)
                }
                ResponseEntity.ok("User checked and created if necessary")
            } catch (e: Exception) {
                ResponseEntity.status(500).body("Error during user check: ${e.message}")
            }
        }

        @GetMapping("/exists")
        fun nicknameExists(
            @RequestParam nickname: String,
            @AuthenticationPrincipal jwt: Jwt,
            ): ResponseEntity<Boolean> {
            return ResponseEntity.ok(userService.nicknameExists(nickname))
        }
    }
