package snippet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class SnippetApplication {
    @GetMapping("/")
    fun noAuth(): String {
        println("This is returning the message")
        return "no auth needed"
    }
}
