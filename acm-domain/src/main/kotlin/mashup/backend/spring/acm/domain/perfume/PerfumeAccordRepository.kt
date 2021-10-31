package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.accord.Accord
import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeAccordRepository : JpaRepository<PerfumeAccord, Long> {
    fun findByPerfumeAndAccord(perfume: Perfume, accord: Accord): PerfumeAccord?
}