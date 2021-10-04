package mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date

import mashup.backend.spring.acm.domain.scrap.ScrapingJob
import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class PerfumeReviewScrapingJob(
    val reviewDate: LocalDate,
    override var status: ScrappingJobStatus = ScrappingJobStatus.PROCESSING
) : ScrapingJob(status) {

    fun updateToSuccess() {
        status = ScrappingJobStatus.SUCCESS
    }

    fun updateToFailure() {
        status = ScrappingJobStatus.FAILURE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PerfumeReviewScrapingJob

        if (reviewDate != other.reviewDate) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = reviewDate.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    override fun toString(): String {
        return "PerfumeReviewScrapingJob(reviewDate=$reviewDate, status=$status)"
    }
}