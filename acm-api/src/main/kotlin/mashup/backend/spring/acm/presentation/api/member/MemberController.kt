package mashup.backend.spring.acm.presentation.api.member

import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.application.LoginApplicationService
import mashup.backend.spring.acm.application.login.toLoginResponse
import mashup.backend.spring.acm.application.login.toVo
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.domain.exception.MemberInitializeFailedException
import mashup.backend.spring.acm.domain.exception.MemberNotFoundException
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toMemberInfoResponse
import mashup.backend.spring.acm.presentation.assembler.toVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

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
        @RequestBody loginRequest: LoginRequest,
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
    ): ResponseEntity<ApiResponse<MemberInfoResponse>> {
        return try {
            ResponseEntity.ok(
                ApiResponse.success(
                    data = memberApplicationService.getMemberInfo(memberId).toMemberInfoResponse()
                )
            )
        } catch (e: MemberNotFoundException) {
            log.warn("내 정보 조회 실패", e)
            ResponseEntity.badRequest().body(
                ApiResponse.failure(e.resultCode)
            )
        }
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
    ): ResponseEntity<ApiResponse<Unit>> {
        return try {
            memberApplicationService.initialize(
                memberId = memberId,
                requestVo = memberInitializeRequest.toVo()
            )
            ResponseEntity.ok(
                ApiResponse.success()
            )
        } catch (e: BusinessException) {
            when (e) {
                is MemberInitializeFailedException,
                is MemberNotFoundException
                -> {
                    log.warn("회원 초기화 실패", e)
                    ResponseEntity.badRequest().body(
                        ApiResponse.failure(e.resultCode)
                    )
                }
                else -> throw e
            }
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MemberController::class.java)
    }
}

