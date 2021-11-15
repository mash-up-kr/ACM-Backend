package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesForPresentService(
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    @Cacheable("recommendPresentPerfumes")
    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        // todo : 구현해야 함
        return emptyList()
    }
}