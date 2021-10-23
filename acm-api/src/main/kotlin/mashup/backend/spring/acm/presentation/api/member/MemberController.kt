package mashup.backend.spring.acm.presentation.api.member

import mashup.backend.spring.acm.application.LoginApplicationService
import mashup.backend.spring.acm.application.login.toLoginResponse
import mashup.backend.spring.acm.application.login.toVo
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toMemberInfoResponse
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val loginApplicationService: LoginApplicationService,
    private val memberApplicationService: MemberApplicationService
) {
    /**
     * 로그인
     * 요청한 정보에 맞는 회원이 없으면 준회원으로 새로 가입
     */
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ApiResponse<LoginResponse> {
        return ApiResponse.success(
            data = loginApplicationService.login(loginRequestVo = loginRequest.toVo()).toLoginResponse()
        )
    }

    @GetMapping("/me")
    fun getMe(
        @ApiIgnore @ModelAttribute("memberId") memberId: Long
    ): ApiResponse<MemberInfoResponse> {
        return ApiResponse.success(
            data = memberApplicationService.getMemberInfo(memberId).toMemberInfoResponse()
        )
    }
}

