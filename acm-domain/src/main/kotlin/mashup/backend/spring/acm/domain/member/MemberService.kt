package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.exception.MemberNicknameDuplicatedException
import mashup.backend.spring.acm.domain.exception.MemberNicknameInvalidException
import mashup.backend.spring.acm.domain.exception.MemberNotFoundException
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import mashup.backend.spring.acm.domain.note.NoteGroupService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface MemberService {
    fun findById(memberId: Long): Member?
    fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member?
    fun findDetailById(memberId: Long): MemberDetailVo?
    fun findAllMemberDetail(): List<SimpleMemberDetailVo>
    fun getMembers(pageable: Pageable): Page<MemberDetailVo>
    fun join(idProviderInfo: IdProviderInfo): MemberDetailVo
    fun withdraw()
    fun updateNickname(memberId: Long, nickname: String)
    fun initialize(memberId: Long, requestVo: MemberInitializeRequestVo)
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberDetailRepository: MemberDetailRepository,
    private val noteGroupService: NoteGroupService,
) : MemberService {
    override fun findById(memberId: Long): Member? {
        return memberRepository.findByIdOrNull(memberId)
    }

    override fun findByIdProviderVo(idProviderInfo: IdProviderInfo): Member? =
        memberRepository.findByMemberIdProviders_IdProviderInfo(idProviderInfo)

    override fun findDetailById(memberId: Long): MemberDetailVo =
        getMemberDetailById(memberId)

    override fun findAllMemberDetail(): List<SimpleMemberDetailVo> =
        memberDetailRepository.findByNoteGroupIdsIsNotNull()
            .map { SimpleMemberDetailVo(it) }

    override fun getMembers(pageable: Pageable): Page<MemberDetailVo> =
        memberRepository.findAll(pageable)
            .map { toMemberDetailVo(it) }

    private fun toMemberDetailVo(member: Member): MemberDetailVo = MemberDetailVo(
        member = member,
        noteGroupSimpleVoList = noteGroupService.getNoteGroupsByIdIn(member.memberDetail.noteGroupIds)
    )

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
        ).let {
            MemberDetailVo(
                member = it,
                noteGroupSimpleVoList = noteGroupService.getNoteGroupsByIdIn(it.memberDetail.noteGroupIds)
            )
        }
    }

    private fun getMemberDetailById(memberId: Long): MemberDetailVo =
        memberRepository.findByIdOrNull(memberId)
            ?.let { toMemberDetailVo(it) }
            ?: throw MemberNotFoundException(memberId = memberId)

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