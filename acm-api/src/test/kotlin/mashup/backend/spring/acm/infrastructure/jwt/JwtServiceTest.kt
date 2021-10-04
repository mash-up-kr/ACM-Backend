package mashup.backend.spring.acm.infrastructure.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JwtServiceTest {
    private val sut: JwtService = JwtService(
        tokenIssuer = "tokenIssuer",
        tokenSigningKey = "tokenSigningKey"
    )

    @DisplayName("encode 성공")
    @Test
    fun encode() {
        // given
        val memberId = 1L
        // when
        val actual = sut.encode(memberId)
        // then
        assertThat(actual).isEqualTo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0b2tlbklzc3VlciIsIm1lbWJlcklkIjoxfQ.sqNkP_ZpWgvpYLg67hxa_6wKvBEvrsxJrSCRok8VNnc")
    }

    @DisplayName("decode 성공")
    @Test
    fun decodeSuccess() {
        // given
        val memberId = 1L
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0b2tlbklzc3VlciIsIm1lbWJlcklkIjoxfQ.sqNkP_ZpWgvpYLg67hxa_6wKvBEvrsxJrSCRok8VNnc"
        // when
        val actual = sut.decode(token)
        // then
        assertThat(actual).isEqualTo(memberId)
    }

    @DisplayName("decode 실패 - secret 일치하지 않음")
    @Test
    fun decodeFailure() {
        // given
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0b2tlbklzc3VlciIsIm1lbWJlcklkIjoxfQ.NHtjs-Dh-KVIM78O3zTZMe2xtwKMvmtHkjWp5LzWI38"
        // when
        val actual = sut.decode(token)
        // then
        assertThat(actual).isNull()
    }
}