package mashup.backend.spring.acm.application.search

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.search.SearchType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@ApplicationService
class SearchApplicationService(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService,
) {
    fun search(searchRequestVo: SearchRequestVo): SearchResponseVo {
        return when (searchRequestVo.type) {
            SearchType.ALL -> searchAllByName(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
            SearchType.BRAND -> searchBrandsByName(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
            SearchType.PERFUME -> searchPerfumesByName(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
        }
    }

    fun searchAllByName(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = brandService.searchByName(name = name, pageable = PageRequest.of(0, 1)),
        perfumes = perfumeService.searchByName(name = name, pageable = pageable)
    )

    fun searchBrandsByName(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = brandService.searchByName(name = name, pageable = pageable),
        perfumes = emptyList()
    )

    fun searchPerfumesByName(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = emptyList(),
        perfumes = perfumeService.searchByName(name = name, pageable = pageable)
    )
}