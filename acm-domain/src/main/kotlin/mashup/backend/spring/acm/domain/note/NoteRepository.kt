package mashup.backend.spring.acm.domain.note

import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<Note, Long> {
    fun existsByUrl(url: String): Boolean
    fun findByUrl(url: String): Note?
    fun findFirstByNoteGroupIsNull(): Note?
}