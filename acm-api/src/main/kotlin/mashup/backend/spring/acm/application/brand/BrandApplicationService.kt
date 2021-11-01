package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.presentation.api.recommend.PopularBrand
import mashup.backend.spring.acm.presentation.assembler.toDto
import mashup.backend.spring.acm.presentation.assembler.toPopularBrand

@ApplicationService
class BrandApplicationService(
    private val brandService: BrandService,
) {
    fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)

    fun getPopularBrand(): List<PopularBrand> {
        // FIXME : perfume에 brand id 추가 되는지 확인 후 추가되면
        return brandService.getPopularBrands().map { it.toPopularBrand() }
    }
}