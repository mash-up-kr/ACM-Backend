package mashup.backend.spring.acm.collector.perfume_url_by_brand

import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.scrap.perfume_by_brand.PerfumeBrandScrapingJobService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

/**
 * brand id 기준으로 향수 url 저장
 */
open class PerfumeUrlByBrandCollectorTasklet(
    private val perfumeUrlByBrandScrapingService: PerfumeUrlByBrandScrapingService,
    private val perfumeBrandScrapingJobService: PerfumeBrandScrapingJobService,
    private val brandService: BrandService,
): Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val brand = resolveBrand()
        if (brand == null) {
            log.info("향수 목록을 조회할 브랜드가 없습니다. ")
            return RepeatStatus.FINISHED
        }
        perfumeUrlByBrandScrapingService.scrap(brand)
        return RepeatStatus.FINISHED
    }

    private fun resolveBrand(): Brand? {
        val lastBrandId = perfumeBrandScrapingJobService.getMaxBrandId() ?: 0L
        return brandService.getBrandByIdGreaterThan(brandId = lastBrandId)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeUrlByBrandCollectorTasklet::class.java)
    }
}