package mashup.backend.spring.acm.domain.scrap.perfume_by_brand

import mashup.backend.spring.acm.domain.scrap.ScrapingJob
import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import javax.persistence.Entity

@Entity
class PerfumeBrandScrapingJob(
    val brandId: Long,
    override var status: ScrappingJobStatus = ScrappingJobStatus.PROCESSING
) : ScrapingJob(status) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PerfumeBrandScrapingJob

        if (brandId != other.brandId) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = brandId.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "PerfumeBrandScrapingJob(brandId=$brandId, status=$status)"
    }
}