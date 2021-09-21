package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class Member(
    val memberStatus: MemberStatus,
    @OneToOne(mappedBy = "member")
    val memberDetail: MemberDetail,
    @OneToMany
    @JoinColumn(name = "memberId")
    val memberIdProviders: List<MemberIdProvider>
) : BaseEntity() {

}
