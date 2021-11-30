package mashup.backend.spring.acm.presentation.scheduler.cache

import mashup.backend.spring.acm.application.brand.BrandCacheApplicationService
import mashup.backend.spring.acm.presentation.scheduler.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BrandCacheScheduler(
    private val brandCacheApplicationService: BrandCacheApplicationService,
): Scheduler {
    override fun preApply() {
        getPopularBrands()
    }

    @Scheduled(cron = "0 0 6 * * *")
    fun getPopularBrands() {
        log.info("[BRAND_SCHEDULER] getPopularBrands >> start")
        brandCacheApplicationService.putCacheGetPopularBrands()
        log.info("[BRAND_SCHEDULER] getPopularBrands << end>>")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandCacheScheduler::class.java)
    }
}