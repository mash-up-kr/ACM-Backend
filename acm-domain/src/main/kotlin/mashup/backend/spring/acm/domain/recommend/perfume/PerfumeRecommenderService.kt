package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.Recommender
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PerfumeRecommenderService(
    recommendPerfumesByNoteGroupIdsAndGenderAndAgeService: RecommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
    recommendPerfumesByNoteGroupIdsAndGenderService: RecommendPerfumesByNoteGroupIdsAndGenderService,
    recommendPerfumesByGenderService: RecommendPerfumesByGenderService,
    recommendPerfumesByUnisexGenderService: RecommendPerfumesByUnisexGenderService,
    recommendPerfumesByPopularNoteGroupService: RecommendPerfumesByPopularNoteGroupService,
    recommendMonthlyPerfumesService: RecommendMonthlyPerfumesService,
    recommendPerfumesForPresentService: RecommendPerfumesForPresentService,
    recommendPerfumesByDefaultService: RecommendPerfumesByDefaultService
) {
    private val similarlyPerfumesRecommender = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService
        ))
        .build()

    private val perfumesByGenderRecommender = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService
        ))
        .build()

    private val popularPerfumesRecommender = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 최근 보관함에 많이 담긴 향수
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService
        ))
        .build()

    private val perfumesByOnboardRecommender = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // todo : digging 개발에 따라 추가 추천 로직 개발해야 함
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService,
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService
        ))
        .build()

    private val perfumesByNoteGroupRecommender = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
        // TODO : 디깅 개발하면서 추가 개발해야 함.
        recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
        recommendPerfumesByNoteGroupIdsAndGenderService,
        recommendPerfumesForPresentService,
        ))
        .build()

    fun recommendSimilarlyPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(similarlyPerfumesRecommender, memberDetailVo, size)

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun recommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByGenderRecommender, memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun cachePutRecommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByGenderRecommender, memberDetailVo, size)

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun recommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(popularPerfumesRecommender, memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun cachePutRecommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(popularPerfumesRecommender, memberDetailVo, size)

    fun recommendPerfumesByOnboard(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByOnboardRecommender, memberDetailVo, size)

    fun recommendPerfumesByNoteGroup(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByNoteGroupRecommender, memberDetailVo, size)


    private fun recommend(recommender: Recommender<Perfume>, memberDetailVo: MemberDetailVo, size: Int) = recommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }
}