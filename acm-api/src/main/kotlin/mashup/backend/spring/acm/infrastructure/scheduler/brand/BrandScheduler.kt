package mashup.backend.spring.acm.infrastructure.scheduler.brand

import mashup.backend.spring.acm.application.brand.BrandApplicationService
import mashup.backend.spring.acm.infrastructure.CacheType
import mashup.backend.spring.acm.infrastructure.cache.CacheService
import mashup.backend.spring.acm.infrastructure.scheduler.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BrandScheduler(
    private val brandApplicationService: BrandApplicationService,
    private val cacheService: CacheService
    ): Scheduler {
    override fun init() {
        getPopularBrands()
    }

    @Scheduled(cron = "0 0 6 * * *")
    fun getPopularBrands() {
        log.info("[BRAND_SCHEDULER] getPopularBrands >> start")
        cacheService.clear(CacheType.POPULAR_BRANDS)
        brandApplicationService.getPopularBrands()
        log.info("[BRAND_SCHEDULER] getPopularBrands << end>>")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandScheduler::class.java)
    }
}