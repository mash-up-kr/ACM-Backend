package mashup.backend.spring.acm.domain.member.idprovider

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
) {

}