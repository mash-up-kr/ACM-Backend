package mashup.backend.spring.acm.domain.perfume

import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeNoteRepository: JpaRepository<PerfumeNote, Long> {
}