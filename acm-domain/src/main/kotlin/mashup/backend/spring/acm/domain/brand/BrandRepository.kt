package mashup.backend.spring.acm.domain.brand

import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByUrl(url: String): Boolean
    fun findByNameContaining(name: String): List<Brand>
}