package mashup.backend.spring.acm.domain

enum class ResultCode(val message: String = "") {
    SUCCESS("성공"),
    // authorization
    UNAUTHORIZED("인증 실패"),
    FORBIDDEN("권한 없음"),
    // member
    MEMBER_NOT_FOUND("회원 조회 실패"),
    ;
}