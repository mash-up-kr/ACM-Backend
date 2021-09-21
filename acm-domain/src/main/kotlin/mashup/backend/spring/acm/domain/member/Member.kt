package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import javax.persistence.*

@Entity
class Member(
    val memberStatus: MemberStatus = MemberStatus.ACTIVE,
    @OneToOne(cascade = [CascadeType.ALL])
    val memberDetail: MemberDetail,
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "memberId")
    val memberIdProviders: MutableList<MemberIdProvider> = ArrayList()
) : BaseEntity() {
    fun add(memberIdProvider: MemberIdProvider) {
        memberIdProviders.add(memberIdProvider)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (memberStatus != other.memberStatus) return false
        if (memberDetail != other.memberDetail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberStatus.hashCode()
        result = 31 * result + memberDetail.hashCode()
        return result
    }

    override fun toString(): String {
        return "Member(memberStatus=$memberStatus, memberDetail=$memberDetail)"
    }

}
