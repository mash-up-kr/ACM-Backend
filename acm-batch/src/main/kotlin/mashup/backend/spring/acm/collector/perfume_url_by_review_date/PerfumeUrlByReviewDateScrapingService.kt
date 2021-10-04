package mashup.backend.spring.acm.collector.perfume_url_by_review_date

import mashup.backend.spring.acm.domain.scrap.AbstractScrapingService
import mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date.PerfumeReviewScrapingJob
import mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date.PerfumeReviewScrapingJobService
import mashup.backend.spring.acm.domain.scrap.perfume_url.PerfumeUrlScrapingJobService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

open class PerfumeUrlByReviewDateScrapingService(
    private val perfumeReviewScrapingJobService: PerfumeReviewScrapingJobService,
    private val perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService
) : AbstractScrapingService<PerfumeReviewScrapingJob, LocalDate>() {

    override fun preProcess(request: LocalDate): PerfumeReviewScrapingJob? {
        super.preProcess(request)
        return perfumeReviewScrapingJobService.createJob(request)
    }

    override fun process(request: LocalDate) {
        super.process(request)
        val document = getDocument(request)

        val dateText =
            document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div.cell.text-center > h3")
                .text()
        log.info("dateText: $dateText")

        val perfumeUrls = getPerfumeUrls(document)
        log.info("perfumeDocuments.size: ${perfumeUrls.size}")

        val countOfSavedPerfumeUrl = perfumeUrls.map { perfumeUrlScrapingJobService.createIfNotExists(it) }
            .count { it.first }
        log.info("countOfSavedPerfumeUrl: $countOfSavedPerfumeUrl")
    }

    private fun getDocument(reviewDate: LocalDate): Document =
        Jsoup.connect("https://www.fragrantica.com/perfume-reviews/")
            .headers(
                mapOf(
                    Pair("authority", "www.fragrantica.com"),
                    Pair("cache-control", "max-age=0"),
                    Pair("sec-ch-ua", "\"Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93\""),
                    Pair("sec-ch-ua-mobile", "?0"),
                    Pair("sec-ch-ua-platform", "\"macOS\""),
                    Pair("upgrade-insecure-requests", "1"),
                    Pair("origin", "https://www.fragrantica.com"),
                    Pair("content-type", "application/x-www-form-urlencoded"),
                    Pair("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36"),
                    Pair("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"),
                    Pair("sec-fetch-site", "same-origin"),
                    Pair("sec-fetch-mode", "navigate"),
                    Pair("sec-fetch-user", "?1"),
                    Pair("sec-fetch-dest", "document"),
                    Pair("referer", "https://www.fragrantica.com/perfume-reviews/"),
                    Pair("accept-language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7"),
                )
            )
            .data("datum", format(reviewDate))
            .post()

    private fun format(reviewDate: LocalDate): String {
        val format = reviewDate.format(DateTimeFormatter.ofPattern("dd LLL yyyy", Locale.US))
        log.debug("reviewDateFormat: $format")
        return format
    }

    private fun getPerfumeUrls(document: Document): Set<String> =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div > div > h2 > a")
            .map { it.attr("href") }
            .toSet()

    override fun postProcess(isSuccess: Boolean, scrapingJob: PerfumeReviewScrapingJob?) {
        super.postProcess(isSuccess, scrapingJob)
        scrapingJob?.run {
            if (isSuccess) {
                perfumeReviewScrapingJobService.updateToSuccess(this.id)
            } else {
                perfumeReviewScrapingJobService.updateToFailure(this.id)
            }
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}