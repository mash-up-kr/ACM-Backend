package mashup.backend.spring.acm.domain.member.idprovider

/**
 * 인증제공자
 */
enum class IdProviderType(
    private val description: String
){
    UUID("익명 인증"),
    APPLE("애플 로그인"),
    KAKAO("카카오 로그인"),
}