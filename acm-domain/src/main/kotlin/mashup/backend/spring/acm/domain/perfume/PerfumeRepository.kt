package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.brand.Brand
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PerfumeRepository : JpaRepository<Perfume, Long> {
    fun existsByUrl(url: String): Boolean
    fun findByUrl(url: String): Perfume?
    fun findPerfumeById(id: Long): Perfume?
    @Query(value = "SELECT p FROM Perfume p WHERE p.gender = :gender ORDER BY function('RAND')")
    fun findByGenderOrderByRandom(gender: Gender, pageable: Pageable): List<Perfume>
    @Query(value="SELECT p FROM Perfume p WHERE p.brand.id = :brandId ORDER BY function('RAND')")
    fun findByBrand_IdOrderByRandom(brandId: Long, pageable: Pageable): List<Perfume>
    fun findByNameContaining(name: String, pageable: Pageable): List<Perfume>
    fun findByBrand(brand: Brand): List<Perfume>
}
