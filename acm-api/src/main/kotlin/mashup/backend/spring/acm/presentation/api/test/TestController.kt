package mashup.backend.spring.acm.presentation.api.test

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.infrastructure.jwt.JwtService
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(
    description = "테스트 편의 기능",
    tags = ["test"],
)
@Profile("local", "develop")
@RestController
@RequestMapping("/api/v1/test")
class TestController(
    private val jwtService: JwtService,
) {
    @ApiOperation(
        value = "memberId 로 accessToken 조회",
    )
    @GetMapping("/token")
    fun getToken(@RequestParam memberId: Long): String = jwtService.encode(memberId)

    @ApiOperation(
        value = "accessToken 으로 memberId 조회",
    )
    @GetMapping("/member-id")
    fun getMemberId(@RequestParam token: String): Long? = jwtService.decode(token)
}