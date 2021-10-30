package mashup.backend.spring.acm.domain.scrap.brand_url

import mashup.backend.spring.acm.domain.exception.ScrapingJobDuplicatedException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BrandPrefixScrapingJobService(
    private val brandPrefixScrapingJobRepository: BrandPrefixScrapingJobRepository,
) {
    fun existsByPrefix(prefix: String): Boolean = brandPrefixScrapingJobRepository.existsByPrefix(prefix)

    @Transactional
    fun createJob(prefix: String): BrandPrefixScrapingJob {
        if (brandPrefixScrapingJobRepository.existsByPrefix(prefix)) {
            throw ScrapingJobDuplicatedException("brandPrefixScrapingJob already exists. prefix: $prefix")
        }
        return brandPrefixScrapingJobRepository.save(
            BrandPrefixScrapingJob(prefix = prefix)
        )
    }

    @Transactional
    fun updateToSuccess(brandPrefixScrapingJobId: Long) =
        brandPrefixScrapingJobRepository.findByIdOrNull(brandPrefixScrapingJobId)?.updateToSuccess()

    @Transactional
    fun updateToFailure(brandPrefixScrapingJobId: Long) =
        brandPrefixScrapingJobRepository.findByIdOrNull(brandPrefixScrapingJobId)?.updateToFailure()
}