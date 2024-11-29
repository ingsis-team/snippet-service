package snippet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleController {

    @GetMapping("/example")
    fun exampleEndpoint(): String {
        return "Hello, World!"
    }
}