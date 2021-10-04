package mashup.backend.spring.acm.application

interface TokenService<T> {
    fun encode(memberId: T): String
    fun decode(token: String?): T?
}