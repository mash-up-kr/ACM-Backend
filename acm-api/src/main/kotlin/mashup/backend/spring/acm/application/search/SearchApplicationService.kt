package mashup.backend.spring.acm.application.search

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.search.SearchType
import org.springframework.data.domain.Pageable

@ApplicationService
class SearchApplicationService(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService,
) {
    fun search(searchRequestVo: SearchRequestVo): SearchResponseVo {
        return when (searchRequestVo.type) {
            SearchType.ALL -> searchAll(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
            SearchType.BRAND -> searchWithBrand(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
            SearchType.PERFUME -> searchWithPerfume(name = searchRequestVo.name, pageable = searchRequestVo.pageable)
        }
    }

    fun searchAll(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = brandService.searchOneByName(name = name),
        perfumes = perfumeService.searchByName(name = name, pageable = pageable)
    )

    fun searchWithBrand(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = brandService.searchByName(name = name, pageable = pageable),
        perfumes = emptyList()
    )

    fun searchWithPerfume(name: String, pageable: Pageable): SearchResponseVo = SearchResponseVo(
        brands = emptyList(),
        perfumes = perfumeService.searchByName(name = name, pageable = pageable)
    )
}