package mashup.backend.spring.acm.collector.brand_by_prefix

import mashup.backend.spring.acm.domain.scrap.AbstractScrapingService
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandPrefixScrapingJob
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandPrefixScrapingJobService
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandUrlScrapingJobService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BrandUrlByPrefixScrapingService(
    private val brandPrefixScrapingJobService: BrandPrefixScrapingJobService,
    private val brandUrlScrapingJobService: BrandUrlScrapingJobService,
) : AbstractScrapingService<BrandPrefixScrapingJob, String>() {

    override fun preProcess(request: String): BrandPrefixScrapingJob? {
        super.preProcess(request)
        return brandPrefixScrapingJobService.createJob(prefix = request)
    }

    override fun process(request: String) {
        super.process(request)
        val document = getDocument(brandPrefix = request)

        val brandUrls = getBrandUrls(document)
        log.info("brandDocuments.size: ${brandUrls.size}")

        val countOfSavedBrandUrl = brandUrls.map { brandUrlScrapingJobService.createIfNotExists(it) }
            .count { it.first }
        log.info("countOfSavedBrandUrl: $countOfSavedBrandUrl")
    }

    private fun getDocument(brandPrefix: String): Document =
        Jsoup.connect("https://www.fragrantica.com/designers-$brandPrefix/").get()

    private fun getBrandUrls(document: Document): Set<String> =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div > a")
            .map { it.attr("href") }
            .toSet()

    override fun postProcess(isSuccess: Boolean, scrapingJob: BrandPrefixScrapingJob?) {
        super.postProcess(isSuccess, scrapingJob)
        scrapingJob?.run {
            if (isSuccess) {
                brandPrefixScrapingJobService.updateToSuccess(this.id)
            } else {
                brandPrefixScrapingJobService.updateToFailure(this.id)
            }
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandUrlByPrefixScrapingService::class.java)
    }
}