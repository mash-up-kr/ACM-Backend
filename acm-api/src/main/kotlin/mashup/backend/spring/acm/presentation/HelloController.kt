package mashup.backend.spring.acm.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@ApiIgnore("health check api 는 swagger 문서에 노출하지 않음")
@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello(): String = "Hello, acm-api"
}
