package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.domain.accord.AccordCreateVo
import mashup.backend.spring.acm.domain.accord.AccordService
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeAccordCreateVo
import mashup.backend.spring.acm.domain.perfume.PerfumeCreateVo
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
    lateinit var accordService: AccordService

    @Autowired
    lateinit var perfumeNoteMappingService: PerfumeNoteMappingService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val perfumeUrlScrapingJob = perfumeUrlScrapingJobService.getOneToScrap()
        if (perfumeUrlScrapingJob == null) {
            log.info("조회할 향수가 없습니다.")
            return RepeatStatus.FINISHED
        }
        log.info("perfumeUrlScrapingJob: $perfumeUrlScrapingJob")
        try {
            val document = getDocument(url = perfumeUrlScrapingJob.url)
            val name = getName(document)
            val brand = getBrandUrl(document)
            val perfumeAccordCreateVoList = getAccords(document).map {
                PerfumeAccordCreateVo(
                    accordId = accordService.createIfNotExists(
                        accordCreateVo = AccordCreateVo(
                            name = it.name,
                            textColor = it.textColor ?: "",
                            backgroundColor = it.backgroundColor ?: "",
                        )
                    ).id,
                    width = it.width,
                    opacity = it.opacity
                )
            }
            val perfumeCreateVo = PerfumeCreateVo(
                name = name,
                originalName = name,
                brand = brand,
                originalBrand = brand,
                gender = getGender(document),
                url = perfumeUrlScrapingJob.url,
                thumbnailImageUrl = getThumbnailImageUrl(document),
                imageUrl = getImageUrl(document),
                description = getDescription(document),
                perfumeAccordCreateVoList = perfumeAccordCreateVoList,
            )
            // 향수 생성, 향수 - 어코드 매핑
            val perfume = perfumeService.create(perfumeCreateVo = perfumeCreateVo)
            // 향수 - 노트 매핑
            perfumeNoteMappingService.saveNotes(document = document, perfumeUrl = perfumeUrlScrapingJob.url)
            perfumeUrlScrapingJobService.updateToSuccess(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
            log.info("향수 저장 성공. perfume: $perfume")
        } catch (e: Exception) {
            log.error("향수 크롤링 실패. url: ${perfumeUrlScrapingJob.url}", e)
            perfumeUrlScrapingJobService.updateToFailure(perfumeUrlScrapingJobId = perfumeUrlScrapingJob.id)
        }
        return RepeatStatus.FINISHED
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun getName(document: Document): String = document.select("#toptop > h1").textNodes()[0].text()

    // TODO: brand 적재 완료 후 매핑해야함
    private fun getBrandUrl(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > p > a")
            .attr("href")

    private fun getGender(document: Document): Gender =
        when (val gender = document.select("#toptop > h1")[0].child(0).text()) {
            "for women and men" -> Gender.UNISEX
            "for women" -> Gender.WOMAN
            "for men" -> Gender.MAN
            else -> {
                log.error("Failed to parse gender. gender: $gender")
                Gender.UNKNOWN
            }
        }

    // TODO: detail 페이지에는 섬네일 없음
    private fun getThumbnailImageUrl(document: Document): String = ""

    private fun getDescription(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(5) > div > p:nth-child(1)")
            .text()

    private fun getImageUrl(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div > div > img")
            .attr("src")

    private fun getAccords(document: Document): List<PerfumeAccordVo> =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div")[0].children()
            .map { element ->
                // color: #FFFFFF;background: #CC3300;opacity: 100%;width: 100%;
                val styleMap = element.child(0).attr("style")
                    .split(";")
                    .filter { it.isNotBlank() }
                    .groupBy(
                        { it.split(": ")[0] },
                        { it.split(": ")[1] },
                    ).mapValues { it.value[0] }
                PerfumeAccordVo(
                    name = element.text(),
                    textColor = styleMap["color"],
                    backgroundColor = styleMap["background"],
                    opacity = styleMap["opacity"]?.let { convertPercentToDouble(it) },
                    width = styleMap["width"]?.let { convertPercentToDouble(it) },
                )
            }

    private fun convertPercentToDouble(value: String): Double = value.replace("%", "").toDouble()

    data class PerfumeAccordVo(
        val name: String,
        val textColor: String?,
        val backgroundColor: String?,
        val opacity: Double?,
        val width: Double?,
    )

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeDetailCollectorTasklet::class.java)
    }
}