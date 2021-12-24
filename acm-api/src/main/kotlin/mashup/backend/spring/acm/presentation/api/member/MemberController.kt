package mashup.backend.spring.acm.presentation.api.member

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.LoginApplicationService
import mashup.backend.spring.acm.presentation.assembler.toLoginResponse
import mashup.backend.spring.acm.presentation.assembler.toVo
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toMemberInfoResponse
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@Api(
    description = "회원",
    tags = ["member"],
)
@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val loginApplicationService: LoginApplicationService,
    private val memberApplicationService: MemberApplicationService,
) {
    @ApiOperation(
        value = "로그인",
        notes = "요청한 정보에 맞는 회원이 없으면 준회원으로 새로 가입. 이미 가입한적이 있으면 로그인"
    )
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
    @ApiOperation(
        value = "내 정보 조회",
    )
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
    @ApiOperation(
        value = "닉네임 변경",
    )
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
     * - 400, MEMBER_NOT_FOUND
     */
    @ApiOperation(
        value = "회원 초기화",
        notes = "온보딩 정보 입력 및 회원 상태 변경(ASSOCIATE -> ACTIVE)"
    )
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

