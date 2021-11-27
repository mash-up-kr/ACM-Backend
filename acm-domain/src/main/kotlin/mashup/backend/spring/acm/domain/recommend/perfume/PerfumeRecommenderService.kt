package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.Recommender
import org.springframework.stereotype.Service

@Service
class PerfumeRecommenderService(
    private val similarPerfumesRecommender: Recommender<Perfume>,
    private val perfumesByOnboardRecommender: Recommender<Perfume>,
    private val perfumesByNoteGroupRecommender: Recommender<Perfume>,
) {
    fun recommendSimilarPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(similarPerfumesRecommender, memberDetailVo, size)

    fun recommendPerfumesByOnboard(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByOnboardRecommender, memberDetailVo, size)

    fun recommendPerfumesByNoteGroup(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByNoteGroupRecommender, memberDetailVo, size)

    private fun recommend(recommender: Recommender<Perfume>, memberDetailVo: MemberDetailVo, size: Int) = recommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }
}