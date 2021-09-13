package mashup.backend.spring.acm.domain.perfume

import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeRepository : JpaRepository<Perfume, Long> {
    fun existsByUrl(url: String): Boolean
}
