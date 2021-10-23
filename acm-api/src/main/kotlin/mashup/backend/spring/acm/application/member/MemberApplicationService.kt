package mashup.backend.spring.acm.application.member

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberInitializeRequestVo
import mashup.backend.spring.acm.domain.member.MemberNotFoundException
import mashup.backend.spring.acm.domain.member.MemberService

interface MemberApplicationService {
    fun getMemberInfo(memberId: Long): MemberDetailVo
    fun initialize(memberId: Long, requestVo: MemberInitializeRequestVo)
}

@ApplicationService
class MemberApplicationServiceImpl(
    private val memberService: MemberService,
) : MemberApplicationService {

    /**
     * 회원 정보 조회
     * 존재하지 않으면 에러 발생
     */
    override fun getMemberInfo(memberId: Long): MemberDetailVo {
        return memberService.findDetailById(memberId = memberId) ?: throw MemberNotFoundException(memberId = memberId)
    }

    /**
     * 회원 초기화
     * - 온보딩 데이터 입력
     * - 준회원 -> 정회원으로 변경
     */
    override fun initialize(memberId: Long, requestVo: MemberInitializeRequestVo) {
        memberService.initialize(
            memberId = memberId,
            requestVo = requestVo
        )
    }
}
