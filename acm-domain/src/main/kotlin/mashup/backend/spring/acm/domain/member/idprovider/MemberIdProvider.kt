package mashup.backend.spring.acm.domain.member.idprovider

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class MemberIdProvider(
    /**
     * 인증제공자 종류, 사용자 식별자
     */
    @Embedded
    val idProviderInfo: IdProviderInfo,
    /**
     * 인증제공자 별 사용자 상태
     */
    @Enumerated(EnumType.STRING)
    var memberIdProviderStatus: MemberIdProviderStatus = MemberIdProviderStatus.ACTIVE,
): BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberIdProvider

        if (idProviderInfo != other.idProviderInfo) return false
        if (memberIdProviderStatus != other.memberIdProviderStatus) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idProviderInfo.hashCode()
        result = 31 * result + memberIdProviderStatus.hashCode()
        return result
    }

    override fun toString(): String {
        return "MemberIdProvider(idProviderInfo=$idProviderInfo, memberIdProviderStatus=$memberIdProviderStatus)"
    }


}