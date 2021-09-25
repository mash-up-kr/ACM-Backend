package mashup.backend.spring.acm.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/hello", "/api/hello")
    fun hello(): String = "Hello, acm-api"
}
