package mashup.backend.spring.acm.domain.accord

import org.springframework.data.jpa.repository.JpaRepository

interface AccordRepository : JpaRepository<Accord, Long> {
    fun findByName(name: String): Accord?
}