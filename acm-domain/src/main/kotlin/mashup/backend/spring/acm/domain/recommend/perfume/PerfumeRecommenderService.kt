package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PerfumeRecommenderService(
    private val recommendPerfumesByNoteGroupIdsAndGenderAndAgeService: RecommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
    private val recommendPerfumesByNoteGroupIdsAndGenderService: RecommendPerfumesByNoteGroupIdsAndGenderService,
    private val recommendPerfumesByGenderService: RecommendPerfumesByGenderService,
    private val recommendPerfumesByUnisexGenderService: RecommendPerfumesByUnisexGenderService,
    private val recommendPerfumesByPopularNoteGroupService: RecommendPerfumesByPopularNoteGroupService,
    private val recommendMonthlyPerfumesService: RecommendMonthlyPerfumesService,
    private val recommendPerfumesForPresentService: RecommendPerfumesForPresentService,
    private val recommendPerfumesByDefaultService: RecommendPerfumesByDefaultService
) {
    fun recommendPerfumesByOnboard(memberDetailVo: MemberDetailVo, size: Int) = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // todo : digging 개발에 따라 추가 추천 로직 개발해야 함
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService,
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService
        ))
        .size(size)
        .build()
        .recommend(memberDetailVo)
        .map { PerfumeSimpleVo(it) }

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun recommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService
        ))
        .size(size)
        .build()
        .recommend(memberDetailVo)
        .map { PerfumeSimpleVo(it) }

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun recommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 최근 보관함에 많이 담긴 향수
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService
        ))
        .size(size)
        .build()
        .recommend(memberDetailVo)
        .map { PerfumeSimpleVo(it) }

    fun recommendPerfumesByNoteGroup(memberDetailVo: MemberDetailVo, size: Int) = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 디깅 개발하면서 추가 개발해야 함.
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesForPresentService,
        ))
        .size(size)
        .build()
        .recommend(memberDetailVo)
        .map { PerfumeSimpleVo(it) }
}