package mashup.backend.spring.acm.domain.member

enum class MemberGender(
    private val description: String,
) {
    MALE("남성"),
    FEMALE("여성"),
    UNKNOWN("모름"),
    ;
}