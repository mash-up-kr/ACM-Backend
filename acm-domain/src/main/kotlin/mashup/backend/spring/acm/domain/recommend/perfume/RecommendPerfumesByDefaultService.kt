package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByDefaultService(
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return recommendRequestVo.size > 0
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        return DEFAULT_RECOMMEND_PERFUMES_URL
            .map { perfumeService.getPerfumeByUrl(it) }
            .take(recommendRequestVo.size)
    }
}

val DEFAULT_RECOMMEND_PERFUMES_URL: List<String> = listOf(
    "https://www.fragrantica.com/perfume/Maison-Francis-Kurkdjian/Baccarat-Rouge-540-33519.html",
    "https://www.fragrantica.com/perfume/Henry-Jacques/Onction-Pure-Perfume-64669.html",
    "https://www.fragrantica.com/perfume/Mugler/Alien-707.html",
    "https://www.fragrantica.com/perfume/Yves-Saint-Laurent/Black-Opium-25324.html",
    "https://www.fragrantica.com/perfume/Creed/Aventus-9828.html",
    "https://www.fragrantica.com/perfume/Tom-Ford/Black-Orchid-1018.html",
    "https://www.fragrantica.com/perfume/Chanel/Coco-Mademoiselle-611.html",
    "https://www.fragrantica.com/perfume/Giorgio-Armani/Acqua-di-Gio-Profumo-29727.html",
    "https://www.fragrantica.com/perfume/Armaf/Club-de-Nuit-Intense-Man-34696.html",
    "https://www.fragrantica.com/perfume/Dior/Sauvage-31861.html"
)
