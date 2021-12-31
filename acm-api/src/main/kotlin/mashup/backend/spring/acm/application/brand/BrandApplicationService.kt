package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.brand.BrandSimpleVo

interface BrandApplicationService {
    fun getBrand(brandId: Long): BrandDetailVo
    fun findPopularBrands(): List<BrandSimpleVo>
}

@ApplicationService
class BrandApplicationServiceImpl(
    private val brandService: BrandService,
): BrandApplicationService {
    override fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)

    override fun findPopularBrands(): List<BrandSimpleVo> {
        return brandService.getPopularBrands()
    }
}