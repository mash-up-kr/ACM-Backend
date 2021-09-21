package mashup.backend.spring.acm.domain.member.idprovider

import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class IdProviderInfo(
    /**
     * 인증제공자 종류
     */
    @Enumerated(EnumType.STRING)
    val idProviderType: IdProviderType,
    /**
     * 인증제공자 사용자 식별자
     */
    val idProviderUserId: String,
)