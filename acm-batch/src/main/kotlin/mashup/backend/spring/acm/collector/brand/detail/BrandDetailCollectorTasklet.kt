package mashup.backend.spring.acm.collector.brand.detail

import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandUrlScrapingJobService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

open class BrandDetailCollectorTasklet : Tasklet {
    @Autowired
    lateinit var brandUrlScrapingJobService: BrandUrlScrapingJobService

    @Autowired
    lateinit var brandService: BrandService

    @Autowired
    lateinit var brandDetailParser: BrandDetailParser

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val brandUrlScrapingJob = brandUrlScrapingJobService.findFirstByUrlForScrap()
        if (brandUrlScrapingJob == null) {
            log.info("조회할 브랜드가 없습니다.")
            return RepeatStatus.FINISHED
        }
        try {
            val brandCreateVo = brandDetailParser.parse(url = brandUrlScrapingJob.url)
            val brand = brandService.create(brandCreateVo = brandCreateVo)
            brandUrlScrapingJobService.updateToSuccess(brandUrlScrapingJobId = brandUrlScrapingJob.id)
            log.info("브랜드 저장 성공. brand: $brand")
        } catch (e: Exception) {
            log.error("브랜드 크롤링 실패. url: ${brandUrlScrapingJob.url}", e)
            brandUrlScrapingJobService.updateToFailure(brandUrlScrapingJobId = brandUrlScrapingJob.id)
        }
        return RepeatStatus.FINISHED
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandDetailCollectorTasklet::class.java)
    }
}