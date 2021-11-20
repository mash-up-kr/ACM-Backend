package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.BaseEntity

class RecommenderBuilder<T : BaseEntity>(
    private var recommendServices: List<RecommendService<T>> = emptyList()
) {
    fun recommendService(recommendServices: List<RecommendService<T>>) = apply { this.recommendServices = recommendServices }

    fun build(): Recommender<T> {
        if (recommendServices.isEmpty()) {
            throw IllegalArgumentException()
        }
        return Recommender(
            recommendServices = recommendServices,
        )
    }
}