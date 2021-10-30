package mashup.backend.spring.acm.domain.scrap.brand_url

import org.springframework.data.jpa.repository.JpaRepository

interface BrandPrefixScrapingJobRepository: JpaRepository<BrandPrefixScrapingJob, Long> {
    fun existsByPrefix(prefix: String): Boolean
}