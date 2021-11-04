package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.scrap.perfume_url.PerfumeUrlScrapingJobService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

open class PerfumeDetailCollectorTasklet : Tasklet {
    @Autowired
    lateinit var perfumeService: PerfumeService

    @Autowired
    lateinit var perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService

    @Autowired
    lateinit var perfumeNoteMappingService: PerfumeNoteMappingService

    @Autowired
    lateinit var perfumeBrandMappingService: PerfumeBrandMappingService

    @Autowired
    lateinit var perfumeDetailParser: PerfumeDetailParser

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val perfumeUrlScrapingJob = perfumeUrlScrapingJobService.getOneToScrap()
        if (perfumeUrlScrapingJob == null) {
            log.info("조회할 향수가 없습니다.")
            return RepeatStatus.FINISHED
        }
        log.info("perfumeUrlScrapingJob: $perfumeUrlScrapingJob")
        val perfumeUrl = perfumeUrlScrapingJob.url
        try {
            val document = getDocument(url = perfumeUrl)
            val perfumeCreateVo = perfumeDetailParser.parse(
                document = document,
                perfumeUrl = perfumeUrl
            )
            // 향수 생성, 향수 - 어코드 매핑
            val perfume = perfumeService.create(perfumeCreateVo = perfumeCreateVo)
            // 향수 - 노트 매핑
            perfumeNoteMappingService.saveNotes(
                document = document,
                perfumeUrl = perfumeUrlScrapingJob.url
            )
            // 향수 - 브랜드 매핑
            val brand = perfumeBrandMappingService.saveBrand(
                perfumeUrl = perfumeUrlScrapingJob.url,
                brandUrl = getBrandUrl(document),
            )
            perfumeUrlScrapingJobService.updateToSuccess(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
            log.info("향수 저장 성공. perfume: $perfume")
        } catch (e: Exception) {
            log.error("향수 크롤링 실패. url: ${perfumeUrlScrapingJob.url}", e)
            perfumeUrlScrapingJobService.updateToFailure(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
        }
        return RepeatStatus.FINISHED
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun getBrandUrl(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > p > a")
            .attr("href")

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeDetailCollectorTasklet::class.java)
    }
}
