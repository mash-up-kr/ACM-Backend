package mashup.backend.spring.acm.collector.perfume_url_by_brand

import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.scrap.AbstractScrapingService
import mashup.backend.spring.acm.domain.scrap.perfume_by_brand.PerfumeBrandScrapingJob
import mashup.backend.spring.acm.domain.scrap.perfume_by_brand.PerfumeBrandScrapingJobService
import mashup.backend.spring.acm.domain.scrap.perfume_url.PerfumeUrlScrapingJobService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PerfumeUrlByBrandScrapingService(
    private val perfumeBrandScrapingJobService: PerfumeBrandScrapingJobService,
    private val perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService,
) : AbstractScrapingService<PerfumeBrandScrapingJob, Brand>() {

    override fun preProcess(request: Brand): PerfumeBrandScrapingJob? {
        super.preProcess(request)
        return perfumeBrandScrapingJobService.createJob(request.id)
    }

    override fun process(request: Brand) {
        super.process(request)
        log.info("brand: $request")

        val document = getDocument(url = request.url)
        val perfumeUrls = document.select("#brands > div.px1-box-shadow > div > div.flex-child-auto > h3 > a")
            .map { it.attr("href") }
            .map { correctUrl(it) }
        log.info("perfumeDocuments.size: ${perfumeUrls.size}")

        val countOfSavedPerfumeUrl = perfumeUrls.map { perfumeUrlScrapingJobService.createIfNotExists(it) }
            .count { it.first }
        log.info("countOfSavedPerfumeUrl: $countOfSavedPerfumeUrl")
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun correctUrl(perfumeUrl: String): String {
        if (perfumeUrl.startsWith("https://www.fragrantica.com")) {
            return perfumeUrl
        }
        return "https://www.fragrantica.com$perfumeUrl"
    }

    override fun postProcess(isSuccess: Boolean, scrapingJob: PerfumeBrandScrapingJob?) {
        super.postProcess(isSuccess, scrapingJob)
        scrapingJob?.run {
            if (isSuccess) {
                perfumeBrandScrapingJobService.updateToSuccess(this.id)
            } else {
                perfumeBrandScrapingJobService.updateToFailure(this.id)
            }
        }
    }
}