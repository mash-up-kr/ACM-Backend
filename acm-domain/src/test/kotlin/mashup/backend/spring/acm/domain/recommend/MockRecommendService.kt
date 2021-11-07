package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.perfume.Perfume

class MockRecommendService : RecommendService<Perfume> {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean = true

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> = emptyList()
}