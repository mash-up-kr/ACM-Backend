package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByMemberIdProviders_IdProviderInfo(idProviderInfo: IdProviderInfo): Member?
    fun findByIdAndMemberStatus(memberId: Long, memberStatus: MemberStatus): Member?
    fun existsByMemberDetail_name(name: String): Boolean
}

