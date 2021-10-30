package mashup.backend.spring.acm.domain.scrap.brand_url

import mashup.backend.spring.acm.domain.scrap.ScrapingJob
import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import javax.persistence.Entity

@Entity
class BrandUrlScrapingJob(
    val url: String,
    override var status: ScrappingJobStatus = ScrappingJobStatus.PROCESSING,
) : ScrapingJob(status) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BrandUrlScrapingJob

        if (url != other.url) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "BrandUrlScrapingJob(url='$url', status=$status)"
    }
}