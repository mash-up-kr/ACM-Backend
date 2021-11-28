package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.perfume.Perfume
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RecommendConfig {
    @Bean
    fun perfumeRecommenderByGender(): Recommender<Perfume> = RecommenderBuilder<Perfume>()
        .recommendService(listOf(
            mockRecommendService(),
        ))
        .build()

    @Bean
    fun mockRecommendService(): RecommendService<Perfume> = MockRecommendService()
}