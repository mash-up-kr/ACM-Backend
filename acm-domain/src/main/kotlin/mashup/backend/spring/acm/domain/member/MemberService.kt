package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface MemberService {
    fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member?
    fun join(): Member
    fun withdraw()
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository
): MemberService {
    override fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member? {
        return memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)
    }

    @Transactional
    override fun join(): Member {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun withdraw() {
        TODO("Not yet implemented")
    }
}