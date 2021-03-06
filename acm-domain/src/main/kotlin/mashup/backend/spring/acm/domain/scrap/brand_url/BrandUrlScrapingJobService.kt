package mashup.backend.spring.acm.domain.scrap.brand_url

import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BrandUrlScrapingJobService(
    private val brandUrlScrapingJobRepository: BrandUrlScrapingJobRepository,
) {
    @Transactional
    fun createIfNotExists(brandUrl: String): Pair<Boolean, BrandUrlScrapingJob?> {
        val url = if (brandUrl.startsWith("https://www.fragrantica.com/")) {
            brandUrl
        } else {
            "https://www.fragrantica.com$brandUrl"
        }
        if (brandUrlScrapingJobRepository.existsByUrl(url)) {
            return Pair(false, null)
        }
        val brandUrlScrapingJob = brandUrlScrapingJobRepository.save(BrandUrlScrapingJob(url))
        return Pair(true, brandUrlScrapingJob)
    }

    @Transactional
    fun updateToSuccess(brandUrlScrapingJobId: Long) =
        brandUrlScrapingJobRepository.findByIdOrNull(brandUrlScrapingJobId)?.updateToSuccess()

    @Transactional
    fun updateToFailure(brandUrlScrapingJobId: Long) =
        brandUrlScrapingJobRepository.findByIdOrNull(brandUrlScrapingJobId)?.updateToFailure()

    fun findFirstByUrlForScrap(): BrandUrlScrapingJob? =
        brandUrlScrapingJobRepository.findFirstByStatus(status = ScrappingJobStatus.WAITING)
}