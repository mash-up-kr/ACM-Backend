package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.exception.MemberNicknameDuplicatedException
import mashup.backend.spring.acm.domain.exception.MemberNicknameInvalidException
import mashup.backend.spring.acm.domain.exception.MemberNotFoundException
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
    fun updateNickname(memberId: Long, nickname: String)
    fun initialize(memberId: Long, requestVo: MemberInitializeRequestVo)
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
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
            ?: throw MemberNotFoundException(memberId = memberId)
        return MemberDetailVo(member)
    }

    @Transactional
    override fun withdraw() {
        TODO("Not yet implemented")
    }

    /**
     * 닉네임 수정
     */
    @Transactional
    override fun updateNickname(memberId: Long, nickname: String) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw MemberNotFoundException(memberId = memberId)
        if (member.memberDetail.name == nickname) {
            return
        }
        validateNickname(nickname)
        member.updateNickname(nickname)
    }

    /**
     * 닉네임 검증
     */
    private fun validateNickname(nickname: String) {
        if (memberRepository.existsByMemberDetail_name(name = nickname)) {
            throw MemberNicknameDuplicatedException(nickname = nickname)
        }
        if (nickname.isBlank()) {
            throw MemberNicknameInvalidException(nickname = nickname)
        }
        if (nickname.length > 50) {
            throw MemberNicknameInvalidException(nickname = nickname)
        }
    }

    @Transactional
    override fun initialize(memberId: Long, requestVo: MemberInitializeRequestVo) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw MemberNotFoundException(memberId = memberId)
        member.initialize(requestVo)
    }
}