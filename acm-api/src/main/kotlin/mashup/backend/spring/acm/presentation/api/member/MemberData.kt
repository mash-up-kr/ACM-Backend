package mashup.backend.spring.acm.presentation.api.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType

data class MemberInfoResponse(
    val member: MemberDetailResponse
)

data class MemberDetailResponse(
    val id: Long,
    /**
     * 회원 상태
     */
    val status: String,
    /**
     * 이름
     */
    val name: String?,
    /**
     * 성별
     * FEMALE, MALE, UNKNOWN
     */
    val gender: String?,
    /**
     * 나이
     */
    val age: Int?
)

data class LoginResponse(
    val accessToken: String,
    val member: MemberDetailResponse
)

data class LoginRequest(
    val idProviderType: IdProviderType,
    val idProviderUserId: String
)