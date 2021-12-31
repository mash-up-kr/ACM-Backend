package mashup.backend.spring.acm.application.brand

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.brand.BrandSimpleVo
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

interface BrandCacheApplicationService {
    fun getPopularBrands(): List<BrandSimpleVo>
    fun putCacheGetPopularBrands(): List<BrandSimpleVo>
}

@ApplicationService
class BrandCacheApplicationServiceImpl(
    private val brandApplicationService: BrandApplicationService,
) : BrandCacheApplicationService {
    @Cacheable(CacheType.CacheNames.POPULAR_BRANDS)
    override fun getPopularBrands() = brandApplicationService.findPopularBrands()

    @CachePut(CacheType.CacheNames.POPULAR_BRANDS)
    override fun putCacheGetPopularBrands() = brandApplicationService.findPopularBrands()
}