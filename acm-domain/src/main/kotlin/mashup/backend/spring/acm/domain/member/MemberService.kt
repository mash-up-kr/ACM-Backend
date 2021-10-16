package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface MemberService {
    fun findById(memberId: Long): Member?
    fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member?
    fun findDetailById(memberId: Long): MemberDetailVo?
    fun join(idProviderInfo: IdProviderInfo): MemberDetailVo
    fun withdraw()
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {
    override fun findById(memberId: Long): Member? {
        return memberRepository.findByIdOrNull(memberId)
    }

    override fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member? {
        return memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)
    }

    override fun findDetailById(memberId: Long): MemberDetailVo {
        return getMemberDetailById(memberId)
    }

    @Transactional
    override fun join(idProviderInfo: IdProviderInfo): MemberDetailVo {
        val member = memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)
        if (member != null) {
            return getMemberDetailById(memberId = member.id)
        }
        return memberRepository.save(
            Member(
                memberIdProviders = arrayListOf(
                    MemberIdProvider(idProviderInfo = idProviderInfo)
                )
            )
        ).let { MemberDetailVo(it) }
    }

    private fun getMemberDetailById(memberId: Long): MemberDetailVo {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw RuntimeException("member not found. memberId: $memberId")
        return MemberDetailVo(member)
    }

    @Transactional
    override fun withdraw() {
        TODO("Not yet implemented")
    }
}