package mashup.backend.spring.acm.domain.member

enum class MemberStatus(
    private val description: String
) {
    ACTIVE("일반회원"),
    ASSOCIATE("준회원"),
    WITHDRAWAL("탈퇴회원"),
}