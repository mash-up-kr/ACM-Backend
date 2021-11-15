package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.application.recommend.RecommendApplicationServiceImpl.Companion.DEFAULT_RECOMMEND_PERFUMES_COUNT
import mashup.backend.spring.acm.domain.brand.BrandDetailVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.presentation.api.recommend.PopularBrand
import mashup.backend.spring.acm.presentation.assembler.toPopularBrand
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume
import org.springframework.cache.annotation.Cacheable

interface BrandApplicationService {
    fun getBrand(brandId: Long): BrandDetailVo
    fun getPopularBrands(): List<PopularBrand>
}

@ApplicationService
class BrandApplicationServiceImpl(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService
): BrandApplicationService {
    override fun getBrand(brandId: Long): BrandDetailVo = brandService.getDetail(brandId = brandId)


    @Cacheable("popularBrands")
    override fun getPopularBrands(): List<PopularBrand> {
        return brandService.getPopularBrands()
            .map { brand ->
                brand.toPopularBrand(
                    perfumeService.getPerfumesByBrandIdWithRandom(brand.id, DEFAULT_RECOMMEND_PERFUMES_COUNT)
                        .map { it.toSimpleRecommendPerfume() }
                )
            }
    }
}