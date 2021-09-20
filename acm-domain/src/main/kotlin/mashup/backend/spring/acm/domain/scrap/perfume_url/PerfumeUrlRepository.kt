package mashup.backend.spring.acm.domain.scrap.perfume_url

import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeUrlRepository : JpaRepository<PerfumeUrlScrapingJob, Long> {
    fun existsByUrl(url: String): Boolean
}