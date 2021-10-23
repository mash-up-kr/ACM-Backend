package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderInfo
import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType
import mashup.backend.spring.acm.domain.member.idprovider.MemberIdProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Year

@Transactional
@SpringBootTest
internal class MemberServiceImplTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var sut: MemberService

    @Test
    fun findMember() {
        // given
        val idProviderInfo = IdProviderInfo(
            idProviderType = IdProviderType.UUID,
            idProviderUserId = "uuidUserId"
        )
        createMember(idProviderInfo)
        // when
        val actual = sut.findByIdProviderVo(idProviderInfo)
        // then
        assertThat(actual).isNotNull
        assertThat(actual!!.memberIdProviders[0].idProviderInfo).isEqualTo(idProviderInfo)
    }

    private fun createMember(idProviderInfo: IdProviderInfo) {
        val memberIdProvider = MemberIdProvider(idProviderInfo)
        val member = Member(
            memberDetail = MemberDetail(
                name = "name",
                gender = "gender",
                birthYear = Year.of(1989)
            )
        )
        member.add(memberIdProvider)
        memberRepository.save(member)
    }

    @Test
    fun join() {
        // given
        val idProviderInfo = IdProviderInfo(
            idProviderType = IdProviderType.UUID,
            idProviderUserId = "idProviderUserId"
        )
        // when
        val actual = sut.join(idProviderInfo)
        // then
        assertThat(actual).isNotNull
    }
}