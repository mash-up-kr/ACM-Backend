package mashup.backend.spring.acm.domain

enum class ResultCode(val message: String = "") {
    SUCCESS("성공"),
    // authorization
    UNAUTHORIZED("인증 실패"),
    FORBIDDEN("권한 없음"),
    // 기타
    BAD_REQUEST("요청에 오류가 있습니다"),
    INTERNAL_SERVER_ERROR("서버 에러"),

    // member
    MEMBER_NOT_FOUND("회원 조회 실패"),
    MEMBER_STATUS_ALREADY_ACTIVE("이미 초기화된 회원입니다"),
    MEMBER_NICKNAME_ALREADY_EXIST("이미 사용중인 닉네임입니다"),
    MEMBER_NICKNAME_INVALID("사용할 수 없는 닉네임입니다"),
    // note
    NOTE_NOT_FOUND("노트 조회 실패"),
    NOTE_ALREADY_EXIST("노트 중복"),
    // note group
    NOTE_GROUP_NOT_FOUND("노트 그룹 조회 실패"),
    // perfume
    PERFUME_NOT_FOUND("향수 조회 실패"),
    // brand
    BRAND_ALREADY_EXIST("브랜드 중복"),
    // job
    SCRAPING_JOB_ALREADY_EXIST("스크래핑 작업 중복")
    ;
}