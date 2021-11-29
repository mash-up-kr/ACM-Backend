package mashup.backend.spring.acm.domain.brand

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByUrl(url: String): Boolean
    fun findByNameContaining(name: String, pageable: Pageable): List<Brand>
    fun findByUrl(url: String): Brand?
}