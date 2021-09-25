package mashup.backend.spring.acm.domain.member.idprovider

enum class MemberIdProviderStatus(
    private val description: String
) {
    ACTIVE("사용중"),
    INACTIVE("탈퇴")
}