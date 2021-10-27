package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import mashup.backend.spring.acm.domain.exception.MemberInitializeFailedException
import javax.persistence.*

@Entity
class Member(
    @Enumerated(EnumType.STRING)
    var memberStatus: MemberStatus = MemberStatus.ASSOCIATE,
    @OneToOne(cascade = [CascadeType.ALL])
    val memberDetail: MemberDetail = MemberDetail.empty(),
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "memberId")
    val memberIdProviders: MutableList<MemberIdProvider> = ArrayList()
) : BaseEntity() {
    fun add(memberIdProvider: MemberIdProvider) {
        memberIdProviders.add(memberIdProvider)
    }

    fun initialize(memberInitializeRequestVo: MemberInitializeRequestVo) {
        if (memberStatus == MemberStatus.ACTIVE) {
            throw MemberInitializeFailedException("이미 ACTIVE 상태인 회원입니다. memberId: $id")
        }
        memberStatus = MemberStatus.ACTIVE
        memberDetail.initialize(memberInitializeRequestVo)
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
