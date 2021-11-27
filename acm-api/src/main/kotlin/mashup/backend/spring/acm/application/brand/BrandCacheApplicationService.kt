package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.application.recommend.RecommendApplicationServiceImpl
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.infrastructure.CacheType
import mashup.backend.spring.acm.presentation.api.recommend.PopularBrand
import mashup.backend.spring.acm.presentation.assembler.toPopularBrand
import mashup.backend.spring.acm.presentation.assembler.toSimpleRecommendPerfume
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

interface BrandCacheApplicationService {
    fun getPopularBrands(): List<PopularBrand>
    fun putCacheGetPopularBrands(): List<PopularBrand>
}

@ApplicationService
class BrandCacheApplicationServiceImpl(
    private val brandService: BrandService,
    private val perfumeService: PerfumeService
): BrandCacheApplicationService {
    @Cacheable(CacheType.CacheNames.POPULAR_BRANDS)
    override fun getPopularBrands() = findPopularBrands()

    @CachePut(CacheType.CacheNames.POPULAR_BRANDS)
    override fun putCacheGetPopularBrands() = findPopularBrands()

    private fun findPopularBrands(): List<PopularBrand> {
        return brandService.getPopularBrands()
            .map { brand ->
                brand.toPopularBrand(
                    perfumeService.getPerfumesByBrandIdWithRandom(brand.id,
                        RecommendApplicationServiceImpl.DEFAULT_RECOMMEND_PERFUMES_COUNT
                    )
                        .map { it.toSimpleRecommendPerfume() }
                )
            }
    }
}