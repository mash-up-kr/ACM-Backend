package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.BaseEntity

class RecommenderBuilder<T : BaseEntity>(
    private var recommendServices: List<RecommendService<T>> = emptyList(),
    private var size: Int = 10,
) {
    fun recommendService(recommendServices: List<RecommendService<T>>) = apply { this.recommendServices = recommendServices }

    fun size(size: Int) = apply { this.size = size }

    fun build(): Recommender<T> {
        if (recommendServices.isEmpty()) {
            throw IllegalArgumentException()
        }
        if (size != null) {
            if (size!! <= 0) {
                throw IllegalArgumentException()
            }
        }
        return Recommender(
            recommendServices = recommendServices,
            size = size
        )
    }
}