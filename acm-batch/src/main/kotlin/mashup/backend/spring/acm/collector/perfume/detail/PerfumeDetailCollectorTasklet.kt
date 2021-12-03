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

/**
 * 향수 url 을 입력받아 상세 정보 크롤링
 */
open class PerfumeDetailCollectorTasklet : Tasklet {
    @Autowired
    lateinit var perfumeService: PerfumeService

    @Autowired
    lateinit var perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService

    @Autowired
    lateinit var perfumeDetailParser: PerfumeDetailParser

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val perfumeUrlScrapingJob = perfumeUrlScrapingJobService.setOneToScrap()
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
            // 향수 생성 (어코드, 노트, 브랜드 매핑)
            val perfume = perfumeService.create(perfumeCreateVo = perfumeCreateVo)
            perfumeUrlScrapingJobService.updateToSuccess(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
            log.info("향수 저장 성공. perfume: $perfume")
        } catch (e: Exception) {
            log.error("향수 크롤링 실패. url: $perfumeUrl", e)
            perfumeUrlScrapingJobService.updateToFailure(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
        }
        return RepeatStatus.FINISHED
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()


    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeDetailCollectorTasklet::class.java)
    }
}
