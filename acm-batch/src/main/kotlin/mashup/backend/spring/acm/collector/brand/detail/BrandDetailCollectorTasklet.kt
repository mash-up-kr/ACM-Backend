package mashup.backend.spring.acm.collector.brand.detail

import mashup.backend.spring.acm.domain.brand.BrandCreateVo
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandUrlScrapingJobService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val brandUrlScrapingJob = brandUrlScrapingJobService.findFirstByUrlForScrap()
        if (brandUrlScrapingJob == null) {
            log.info("조회할 브랜드가 없습니다.")
            return RepeatStatus.FINISHED
        }
        try {
            val document = getDocument(url = brandUrlScrapingJob.url)
            brandService.create(
                brandCreateVo = BrandCreateVo(
                    name = getName(document),
                    url = brandUrlScrapingJob.url,
                    description = getDescription(document),
                    logoImageUrl = getLogoImageUrl(document)
                )
            )
            brandUrlScrapingJobService.updateToSuccess(brandUrlScrapingJobId = brandUrlScrapingJob.id)
        } catch (e: Exception) {
            log.error("브랜드 크롤링 실패. url: ${brandUrlScrapingJob.url}", e)
            brandUrlScrapingJobService.updateToFailure(brandUrlScrapingJobId = brandUrlScrapingJob.id)
        }
        return RepeatStatus.FINISHED
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun getName(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.text-center.dname > h1")
            .text()

    private fun getDescription(document: Document): String =
        document.select("#descAAA > p").joinToString(separator = "\n") { it.text() }

    private fun getLogoImageUrl(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.small-12.medium-4 > div > div.cell.small-4.medium-12 > img")
            .attr("src")
            .ifBlank { null }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandDetailCollectorTasklet::class.java)
    }
}