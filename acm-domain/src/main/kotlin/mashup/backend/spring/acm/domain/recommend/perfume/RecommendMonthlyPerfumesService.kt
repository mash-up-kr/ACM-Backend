package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RecommendMonthlyPerfumesService(
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    @Cacheable("recommendMonthlyPerfumes")
    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        return DEFAULT_MONTHLY_RECOMMEND_PERFUMES_URL.map { perfumeService.getPerfumeByUrl(it) }
    }
}

val DEFAULT_MONTHLY_RECOMMEND_PERFUMES_URL: List<String> = listOf(
    "https://www.fragrantica.com/perfume/Eau-de-Space/Eau-de-Luna-The-Smell-of-the-Moon-66566.html",
    "https://www.fragrantica.com/perfume/Cristian-Cavagna/Boa-Madre-69817.html",
    "https://www.fragrantica.com/perfume/Jimmy-Choo/Amber-Kiss-60128.html",
    "https://www.fragrantica.com/perfume/Monart-Parfums/Delice-de-la-vie-66661.html",
    "https://www.fragrantica.com/perfume/Guerlain/Mon-Guerlain-Extrait-58627.html",
    "https://www.fragrantica.com/perfume/SIAM-1928/Mhee-68641.html",
    "https://www.fragrantica.com/perfume/Giardino-Benessere/Iperione-61893.html",
    "https://www.fragrantica.com/perfume/Bel-Rebel/Air-64485.html",
    "https://www.fragrantica.com/perfume/UER-MI/UD-IKAT-66096.html",
    "https://www.fragrantica.com/perfume/Henry-Jacques/Onction-Pure-Perfume-64669.html"
)
