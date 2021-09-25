package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface MemberService {
    fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member?
    fun join(idProviderInfo: IdProviderInfo): Member
    fun withdraw()
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {
    override fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member? {
        return memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)
    }

    @Transactional
    override fun join(idProviderInfo: IdProviderInfo): Member {
        val member = memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)
        if (member != null) {
            return member
        }
        return memberRepository.save(
            Member(
                memberIdProviders = arrayListOf(
                    MemberIdProvider(idProviderInfo = idProviderInfo)
                )
            )
        )
    }

    @Transactional
    override fun withdraw() {
        TODO("Not yet implemented")
    }
}