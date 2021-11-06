package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.BaseEntity

interface RecommendService<T : BaseEntity> {
    fun supports(recommendRequestVo: RecommendRequestVo): Boolean
    fun getItems(recommendRequestVo: RecommendRequestVo): List<T>
}