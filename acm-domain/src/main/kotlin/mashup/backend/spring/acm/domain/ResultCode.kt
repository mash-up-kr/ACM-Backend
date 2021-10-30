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
    MEMBER_STATUS_ALREADY_ACTIVE("이미 초기화된 회원입니다."),
    // note
    NOTE_NOT_FOUND("노트 조회 실패"),
    NOTE_ALREADY_EXIST("노트 중복"),
    // perfume
    PERFUME_NOT_FOUND("향수 조회 실패"),
    // brand
    BRAND_ALREADY_EXIST("브랜드 중복"),
    // job
    SCRAPING_JOB_ALREADY_EXIST("스크래핑 작업 중복")
    ;
}