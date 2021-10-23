package mashup.backend.spring.acm.presentation.api.test

import mashup.backend.spring.acm.infrastructure.jwt.JwtService
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Profile("local", "develop")
@RestController
@RequestMapping("/api/v1/test")
class TestController(
    private val jwtService: JwtService
) {
    @GetMapping("/token")
    fun getToken(@RequestParam memberId: Long): String = jwtService.encode(memberId)

    @GetMapping("/member-id")
    fun getMemberId(@RequestParam token: String): Long? = jwtService.decode(token)
}