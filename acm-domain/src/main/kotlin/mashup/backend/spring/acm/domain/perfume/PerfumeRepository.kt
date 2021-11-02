package mashup.backend.spring.acm.domain.perfume

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeRepository : JpaRepository<Perfume, Long> {
    fun existsByUrl(url: String): Boolean
    fun findByUrl(url: String): Perfume?
    fun findPerfumeById(id: Long): Perfume?
    fun findByNameContaining(name: String): List<Perfume>
}

interface PerfumeNoteRepository : JpaRepository<PerfumeNote, Long> {
    fun findByNote_Id(noteId: Long, pageable: Pageable): Page<PerfumeNote>
    fun findByNote_IdAndPerfume_Gender(noteId: Long, perfumeGender: Gender, pageable: Pageable): Page<PerfumeNote>
}
