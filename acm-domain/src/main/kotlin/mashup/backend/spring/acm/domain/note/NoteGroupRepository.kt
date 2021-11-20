package mashup.backend.spring.acm.domain.note

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface NoteGroupRepository : JpaRepository<NoteGroup, Long> {
    fun existsByOriginalName(originalName: String): Boolean

    fun findByOriginalName(originalName: String): NoteGroup?

    fun findNoteGroupById(id: Long): NoteGroup?

    @Query(value = "SELECT ng FROM NoteGroup ng ORDER BY function('RAND')")
    fun findByRandom(pageable: Pageable): List<NoteGroup>
}