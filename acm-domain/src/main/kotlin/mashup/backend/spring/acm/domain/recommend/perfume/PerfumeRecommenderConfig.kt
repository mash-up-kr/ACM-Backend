package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PerfumeRecommenderConfig(
    private val recommendPerfumesByNoteGroupIdsAndGenderAndAgeService: RecommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
    private val recommendPerfumesByNoteGroupIdsAndGenderService: RecommendPerfumesByNoteGroupIdsAndGenderService,
    private val recommendPerfumesByGenderService: RecommendPerfumesByGenderService,
    private val recommendPerfumesByUnisexGenderService: RecommendPerfumesByUnisexGenderService,
    private val recommendPerfumesByPopularNoteGroupService: RecommendPerfumesByPopularNoteGroupService,
    private val recommendMonthlyPerfumesService: RecommendMonthlyPerfumesService,
    private val recommendPerfumesForPresentService: RecommendPerfumesForPresentService,
    private val recommendPerfumesByDefaultService: RecommendPerfumesByDefaultService
) {

    @Bean
    fun similarPerfumesRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByDefaultService,
        ))
    .build()

    @Bean
    fun perfumesByGenderRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService,
            recommendPerfumesByDefaultService,
        ))
        .build()

    @Bean
    fun popularPerfumesRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 최근 보관함에 많이 담긴 향수
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService,
            recommendPerfumesByDefaultService,
        ))
        .build()

    @Bean
    fun perfumesByOnboardRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // todo : digging 개발에 따라 추가 추천 로직 개발해야 함
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesByGenderService,
            recommendPerfumesByUnisexGenderService,
            recommendPerfumesByPopularNoteGroupService,
            recommendMonthlyPerfumesService,
            recommendPerfumesByDefaultService,
        ))
        .build()

    @Bean
    fun perfumesByNoteGroupRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 디깅 개발하면서 추가 개발해야 함.
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService,
            recommendPerfumesByNoteGroupIdsAndGenderService,
            recommendPerfumesForPresentService,
            recommendPerfumesByDefaultService,
        ))
        .build()

}