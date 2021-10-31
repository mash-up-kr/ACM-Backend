package mashup.backend.spring.acm.application.search

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo

@ApplicationService
class SearchApplicationService(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService,
) {
    fun search(name: String): Pair<List<BrandSimpleVo>, List<PerfumeSimpleVo>> = Pair(
        brandService.findByName(name = name),
        perfumeService.findByName(name = name)
    )
}