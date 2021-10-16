package mashup.backend.spring.acm.application.member

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

interface MemberApplicationService {
    fun getMemberInfo(memberId: Long): MemberDetailVo
}

@Service
class MemberApplicationServiceImpl(
    private val memberService: MemberService
) : MemberApplicationService {

    /**
     * 회원 정보 조회
     * 존재하지 않으면 에러 발생
     */
    override fun getMemberInfo(memberId: Long): MemberDetailVo {
        return memberService.findDetailById(memberId = memberId) ?: throw RuntimeException("member not found")
    }
}
