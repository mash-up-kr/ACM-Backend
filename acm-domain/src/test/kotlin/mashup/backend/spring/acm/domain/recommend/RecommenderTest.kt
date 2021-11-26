package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberGender
import mashup.backend.spring.acm.domain.member.MemberStatus
import mashup.backend.spring.acm.domain.perfume.Perfume
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class RecommenderTest {

    @Autowired
    lateinit var perfumeRecommenderByGender: Recommender<Perfume>

    @Test
    fun recommend() {
        // given
        val memberDetailVo = MemberDetailVo(
            id = 0,
            status = MemberStatus.ACTIVE,
            name = "name",
            gender = MemberGender.MALE,
            ageGroup = AgeGroup.TEENAGER,
            noteGroupIds = emptyList(),
            noteGroupSimpleVoList = emptyList(),
        )
        // when
        val actual = perfumeRecommenderByGender.recommend(memberDetailVo = memberDetailVo, 3)
        // then
        assertTrue(actual.isEmpty())
    }

}
