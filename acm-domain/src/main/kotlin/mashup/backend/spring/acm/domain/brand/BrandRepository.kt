package mashup.backend.spring.acm.domain.brand

import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByUrl(url: String): Boolean
    fun findTop30ByNameContaining(name: String): List<Brand>
    fun findByUrl(url: String): Brand?
}