package mashup.backend.spring.acm.domain.scrap.perfume_by_brand

import mashup.backend.spring.acm.domain.exception.ScrapingJobDuplicatedException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PerfumeBrandScrapingJobService(
    private val perfumeBrandScrapingJobRepository: PerfumeBrandScrapingJobRepository,
) {
    @Transactional
    fun createJob(brandId: Long): PerfumeBrandScrapingJob {
        if (perfumeBrandScrapingJobRepository.existsByBrandId(brandId)) {
            throw ScrapingJobDuplicatedException("perfumeBrandScrapingJob already exists. brandId: $brandId")
        }
        return perfumeBrandScrapingJobRepository.save(
            PerfumeBrandScrapingJob(brandId = brandId)
        )
    }

    @Transactional
    fun updateToSuccess(perfumeBrandScrapingJobId: Long) =
        perfumeBrandScrapingJobRepository.findByIdOrNull(perfumeBrandScrapingJobId)?.updateToSuccess()

    @Transactional
    fun updateToFailure(perfumeBrandScrapingJobId: Long) =
        perfumeBrandScrapingJobRepository.findByIdOrNull(perfumeBrandScrapingJobId)?.updateToFailure()

    fun getMaxBrandId(): Long? = perfumeBrandScrapingJobRepository.findTop1ByOrderByBrandIdDesc()?.brandId
}