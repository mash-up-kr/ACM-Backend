package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService

@ApplicationService
class BrandApplicationService(
    private val brandService: BrandService,
) {
    fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)
}