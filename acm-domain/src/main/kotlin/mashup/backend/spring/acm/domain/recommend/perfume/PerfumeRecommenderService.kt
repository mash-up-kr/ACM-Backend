package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
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

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun recommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) = perfumesByGenderRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun cachePutRecommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) = perfumesByGenderRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun recommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) = popularPerfumesRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun cachePutRecommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) = popularPerfumesRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }

    fun recommendPerfumesByOnboard(memberDetailVo: MemberDetailVo, size: Int) = perfumesByOnboardRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }


    fun recommendPerfumesByNoteGroup(memberDetailVo: MemberDetailVo, size: Int) = perfumesByNoteGroupRecommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }
}