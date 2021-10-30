package mashup.backend.spring.acm.presentation.api.member

import mashup.backend.spring.acm.application.LoginApplicationService
import mashup.backend.spring.acm.presentation.assembler.toLoginResponse
import mashup.backend.spring.acm.presentation.assembler.toVo
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toMemberInfoResponse
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val loginApplicationService: LoginApplicationService,
    private val memberApplicationService: MemberApplicationService,
) {
    /**
     * 로그인
     * 요청한 정보에 맞는 회원이 없으면 준회원으로 새로 가입
     */
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid loginRequest: LoginRequest,
    ): ApiResponse<LoginResponse> {
        return ApiResponse.success(
            data = loginApplicationService.login(loginRequestVo = loginRequest.toVo()).toLoginResponse()
        )
    }

    /**
     * 내 정보 조회
     * - 200, SUCCESS
     * - 400, MEMBER_NOT_FOUND
     * - 401, UNAUTHORIZED
     */
    @GetMapping("/me")
    fun getMe(
        @ApiIgnore @ModelAttribute("memberId") memberId: Long,
    ): ApiResponse<MemberInfoResponse> {
        return ApiResponse.success(
            data = memberApplicationService.getMemberInfo(memberId).toMemberInfoResponse()
        )
    }

    /**
     * 닉네임 변경
     * - 200, SUCCESS
     * - 400, MEMBER_NICKNAME_ALREADY_EXIST
     * - 400, MEMBER_NOT_FOUND
     * - 401, UNAUTHORIZED
     */
    @PutMapping("/me/nickname")
    fun updateNickname(
        @ApiIgnore @ModelAttribute("memberId") memberId: Long,
        @RequestBody nicknameUpdateRequest: NicknameUpdateRequest
    ): ApiResponse<Unit> {
        memberApplicationService.updateNickname(
            memberId = memberId,
            nickname = nicknameUpdateRequest.nickname
        )
        return ApiResponse.success()
    }

    /**
     * 회원 초기화 (온보딩 정보 입력, 상태 변경(ASSOCIATE -> ACTIVE))
     * - 200, SUCCESS
     * - 400, MEMBER_STATUS_ALREADY_ACTIVE
     * - 400, MEMBER_NOT_FOUND
     */
    @PostMapping("/initialize")
    fun initialize(
        @ApiIgnore @ModelAttribute("memberId") memberId: Long,
        @RequestBody memberInitializeRequest: MemberInitializeRequest,
    ): ApiResponse<Unit> {
        memberApplicationService.initialize(
            memberId = memberId,
            requestVo = memberInitializeRequest.toVo()
        )
        return ApiResponse.success()
    }
}

