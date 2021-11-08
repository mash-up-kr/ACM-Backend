package mashup.backend.spring.acm.domain.recommend

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.member.MemberDetailVo

class Recommender<T : BaseEntity>(
    private val recommendServices: List<RecommendService<T>>,
    private val size: Int,
) {
    fun recommend(memberDetailVo: MemberDetailVo): List<T> {
        val perfumes: MutableList<T> = mutableListOf()
        for (recommendService in recommendServices) {
            if (perfumes.size >= size) {
                break
            }
            val recommendRequestVo = RecommendRequestVo(
                memberDetailVo = memberDetailVo,
                size = size - perfumes.size,
                exceptIds = perfumes.map { it.id }.toSet()
            )
            if (recommendService.supports(recommendRequestVo = recommendRequestVo)) {
                perfumes += recommendService.getItems(recommendRequestVo = recommendRequestVo)
            }
        }
        return perfumes.toList()
    }
}
