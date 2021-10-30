package mashup.backend.spring.acm.domain.scrap.brand_url

import mashup.backend.spring.acm.domain.scrap.ScrapingJob
import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import javax.persistence.Entity

@Entity
class BrandPrefixScrapingJob(
    val prefix: String,
    override var status: ScrappingJobStatus = ScrappingJobStatus.PROCESSING,
) : ScrapingJob(status)
