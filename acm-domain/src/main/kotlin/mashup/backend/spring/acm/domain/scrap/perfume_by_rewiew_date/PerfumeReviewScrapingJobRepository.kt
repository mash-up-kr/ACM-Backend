package mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface PerfumeReviewScrapingJobRepository : JpaRepository<PerfumeReviewScrapingJob, Long> {
    fun existsByReviewDate(reviewDate: LocalDate): Boolean
}