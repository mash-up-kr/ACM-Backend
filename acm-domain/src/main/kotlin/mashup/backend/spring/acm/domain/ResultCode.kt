package mashup.backend.spring.acm.domain

enum class ResultCode(val message: String = "") {
    SUCCESS("성공"),
    // authorization
    UNAUTHORIZED("인증 실패"),
    FORBIDDEN("권한 없음"),
    // 기타
    INTERNAL_SERVER_ERROR("서버 에러"),

    // member
    MEMBER_NOT_FOUND("회원 조회 실패"),
    MEMBER_STATUS_ALREADY_ACTIVE("이미 초기화된 회원입니다.")
    ;
}