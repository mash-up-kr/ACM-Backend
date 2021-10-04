package mashup.backend.spring.acm.infrastructure.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import mashup.backend.spring.acm.application.TokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Value("\${jwt.token-issuer}")
    private val tokenIssuer: String,
    @Value("\${jwt.token-signing-key}")
    private val tokenSigningKey: String
) : TokenService<Long> {
    private val algorithm: Algorithm = Algorithm.HMAC256(tokenSigningKey)
    private val jwtVerifier: JWTVerifier = JWT.require(algorithm).build()

    override fun encode(memberId: Long): String {
        return JWT.create()
            .withIssuer(tokenIssuer)
            .withClaim(CLAIM_NAME_MEMBER_ID, memberId)
            .sign(algorithm)
    }

    override fun decode(token: String?): Long? {
        return try {
            jwtVerifier.verify(token).let { it.claims[CLAIM_NAME_MEMBER_ID]?.asLong() }
        } catch (ex: JWTVerificationException) {
            null
        }
    }

    companion object {
        private const val CLAIM_NAME_MEMBER_ID = "memberId"
    }

}