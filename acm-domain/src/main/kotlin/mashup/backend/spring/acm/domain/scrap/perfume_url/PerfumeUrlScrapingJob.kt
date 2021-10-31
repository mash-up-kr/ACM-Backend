package mashup.backend.spring.acm.domain.scrap.perfume_url

import mashup.backend.spring.acm.domain.scrap.ScrapingJob
import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import javax.persistence.Entity

/**
 * 향수 url 목록을 관리함
 */
@Entity
class PerfumeUrlScrapingJob(
    val url: String,
    override var status: ScrappingJobStatus = ScrappingJobStatus.PROCESSING
) : ScrapingJob(status) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PerfumeUrlScrapingJob

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

    override fun toString(): String {
        return "PerfumeUrl(url='$url', status='$status')"
    }
}