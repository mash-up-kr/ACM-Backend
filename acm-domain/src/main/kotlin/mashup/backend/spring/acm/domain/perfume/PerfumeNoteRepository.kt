package mashup.backend.spring.acm.domain.perfume

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeNoteRepository: JpaRepository<PerfumeNote, Long> {
    fun findByNote_Id(noteId: Long, pageable: Pageable): Page<PerfumeNote>
    fun findByNote_IdAndPerfume_Gender(noteId: Long, perfumeGender: Gender, pageable: Pageable): Page<PerfumeNote>
}