package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.application.recommend.RecommendApplicationService.Companion.DEFAULT_RECOMMEND_PERFUMES_COUNT
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.PopularBrand
import mashup.backend.spring.acm.presentation.assembler.toPopularBrand
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume

@ApplicationService
class BrandApplicationService(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService
) {
    fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)

    fun getPopularBrand(): List<PopularBrand> {
        return brandService.getPopularBrands()
            .map { brand ->
                brand.toPopularBrand(
                    perfumeService.getPerfumesByBrandIdWithRandom(brand.id, DEFAULT_RECOMMEND_PERFUMES_COUNT)
                        .map { it.toSimpleRecommendPerfume() }
                )
            }
    }
}