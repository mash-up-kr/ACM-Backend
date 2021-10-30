package mashup.backend.spring.acm.domain.scrap.brand_url

import mashup.backend.spring.acm.domain.scrap.ScrappingJobStatus
import org.springframework.data.jpa.repository.JpaRepository

interface BrandUrlScrapingJobRepository : JpaRepository<BrandUrlScrapingJob, Long> {
    fun existsByUrl(url: String): Boolean
    fun findFirstByStatus(status: ScrappingJobStatus): BrandUrlScrapingJob?
}