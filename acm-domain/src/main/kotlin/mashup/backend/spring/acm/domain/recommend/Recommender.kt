package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.member.MemberDetailVo

class Recommender<T : BaseEntity>(
    private val recommendServices: List<RecommendService<T>>
) {
    fun recommend(memberDetailVo: MemberDetailVo, size: Int): List<T> {
        val items: MutableList<T> = mutableListOf()
        for (recommendService in recommendServices) {
            if (items.size >= size) {
                break
            }
            val recommendRequestVo = RecommendRequestVo(
                memberDetailVo = memberDetailVo,
                size = size - items.size,
                exceptIds = items.map { it.id }.toSet()
            )
            if (recommendService.supports(recommendRequestVo = recommendRequestVo)) {
                items += recommendService.getItems(recommendRequestVo = recommendRequestVo)
            }
        }
        return items.toList()
    }
}
