package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PerfumeRecommenderConfig(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService,
) {
    @Bean
    fun similarPerfumesRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService()
        ))
        .build()

    @Bean
    fun perfumesByGenderRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            recommendPerfumesByNoteGroupIdsAndGenderService(),
            recommendPerfumesByGenderService(),
            recommendPerfumesByUnisexGenderService(),
        ))
        .build()

    @Bean
    fun popularPerfumesRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 최근 보관함에 많이 담긴 향수
            recommendPerfumesByPopularNoteGroupService(),
            recommendMonthlyPerfumesService(),
        ))
        .build()

    @Bean
    fun perfumesByOnboardRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // todo : digging 개발에 따라 추가 추천 로직 개발해야 함
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService(),
            recommendPerfumesByNoteGroupIdsAndGenderService(),
            recommendPerfumesByGenderService(),
            recommendPerfumesByUnisexGenderService(),
            recommendPerfumesByPopularNoteGroupService(),
            recommendMonthlyPerfumesService(),
        ))
        .build()

    @Bean
    fun perfumesByNoteGroupRecommender() = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            // TODO : 디깅 개발하면서 추가 개발해야 함.
            recommendPerfumesByNoteGroupIdsAndGenderAndAgeService(),
            recommendPerfumesByNoteGroupIdsAndGenderService(),
            recommendPerfumesForPresentService(),
        ))
        .build()

    @Bean
    fun recommendPerfumesByNoteGroupIdsAndGenderAndAgeService() = RecommendPerfumesByNoteGroupIdsAndGenderAndAgeService(noteGroupService, perfumeService)

    @Bean
    fun recommendPerfumesByNoteGroupIdsAndGenderService() = RecommendPerfumesByNoteGroupIdsAndGenderService(noteGroupService, perfumeService)

    @Bean
    fun recommendPerfumesByGenderService() = RecommendPerfumesByGenderService(perfumeService)

    @Bean
    fun recommendPerfumesByUnisexGenderService() = RecommendPerfumesByUnisexGenderService(perfumeService)

    @Bean
    fun recommendPerfumesByPopularNoteGroupService() = RecommendPerfumesByPopularNoteGroupService(noteGroupService, perfumeService)

    @Bean
    fun recommendMonthlyPerfumesService() = RecommendPerfumesByPopularNoteGroupService(noteGroupService, perfumeService)

    @Bean
    fun recommendPerfumesForPresentService() = RecommendPerfumesByPopularNoteGroupService(noteGroupService, perfumeService)

    @Bean
    fun recommendPerfumesByDefaultService() = RecommendPerfumesByDefaultService(perfumeService)

}