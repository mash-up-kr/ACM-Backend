package mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date

import mashup.backend.spring.acm.domain.exception.ScrapingJobDuplicatedException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class PerfumeReviewScrapingJobService(
    private val perfumeReviewScrapingJobRepository: PerfumeReviewScrapingJobRepository
) {
    fun existsByReviewDate(reviewDate: LocalDate): Boolean = exists(reviewDate)

    @Transactional
    fun createJob(reviewDate: LocalDate): PerfumeReviewScrapingJob {
        if (exists(reviewDate)) {
            throw ScrapingJobDuplicatedException("perfumeReviewScrapingJob already exists. reviewDate: $reviewDate")
        }
        return perfumeReviewScrapingJobRepository.save(
            PerfumeReviewScrapingJob(reviewDate = reviewDate)
        )
    }

    private fun exists(reviewDate: LocalDate): Boolean =
        perfumeReviewScrapingJobRepository.existsByReviewDate(reviewDate)

    @Transactional
    fun updateToSuccess(perfumeReviewScrapingJobId: Long) =
        perfumeReviewScrapingJobRepository.findByIdOrNull(perfumeReviewScrapingJobId)?.updateToSuccess()

    @Transactional
    fun updateToFailure(perfumeReviewScrapingJobId: Long) =
        perfumeReviewScrapingJobRepository.findByIdOrNull(perfumeReviewScrapingJobId)?.updateToFailure()
}