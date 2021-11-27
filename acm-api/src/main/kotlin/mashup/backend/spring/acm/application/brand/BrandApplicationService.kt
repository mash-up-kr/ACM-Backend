package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService

interface BrandApplicationService {
    fun getBrand(brandId: Long): BrandDetailVo
}

@ApplicationService
class BrandApplicationServiceImpl(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService
): BrandApplicationService {
    override fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)
}