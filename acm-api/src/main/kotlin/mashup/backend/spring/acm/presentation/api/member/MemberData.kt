package mashup.backend.spring.acm.presentation.api.member

import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberGender
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MemberInfoResponse(
    val member: MemberDetailResponse,
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
     * 나이대
     */
    val ageGroup: String?,
)

data class LoginResponse(
    val accessToken: String,
    val member: MemberDetailResponse,
)

data class LoginRequest(
    @field:NotNull
    val idProviderType: IdProviderType?,
    @field:NotBlank
    @field:Length(max = 255)
    val idProviderUserId: String?,
)

data class MemberInitializeRequest(
    val gender: MemberGender?,
    val ageGroup: AgeGroup?,
    val noteGroupIds: List<Long>?,
)

data class NicknameUpdateRequest(
    val nickname: String,
)
