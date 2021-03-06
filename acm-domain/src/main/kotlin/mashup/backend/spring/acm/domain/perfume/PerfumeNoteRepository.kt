package mashup.backend.spring.acm.domain.perfume

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeNoteRepository: JpaRepository<PerfumeNote, Long> {
    fun findByNoteId(noteId: Long, pageable: Pageable): Page<PerfumeNote>
    fun findByNoteIdAndPerfumeGender(noteId: Long, perfumeGender: Gender, pageable: Pageable): Page<PerfumeNote>
    fun findByPerfumeGenderAndNoteNoteGroupId(gender: Gender, noteGroupId: Long, pageable: Pageable): Page<PerfumeNote>
}