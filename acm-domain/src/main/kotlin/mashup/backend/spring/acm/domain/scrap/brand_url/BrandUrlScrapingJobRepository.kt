package mashup.backend.spring.acm.domain.scrap.brand_url

import org.springframework.data.jpa.repository.JpaRepository

interface BrandUrlScrapingJobRepository: JpaRepository<BrandUrlScrapingJob, Long> {
    fun existsByUrl(url: String): Boolean
}