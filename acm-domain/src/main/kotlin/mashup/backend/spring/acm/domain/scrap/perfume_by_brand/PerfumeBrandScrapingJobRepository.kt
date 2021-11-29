package mashup.backend.spring.acm.domain.scrap.perfume_by_brand

import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeBrandScrapingJobRepository: JpaRepository<PerfumeBrandScrapingJob, Long> {
    fun existsByBrandId(brandId: Long): Boolean
    fun findTop1ByOrderByBrandIdDesc(): PerfumeBrandScrapingJob?
}