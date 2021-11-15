package mashup.backend.spring.acm.domain.note

import org.springframework.data.jpa.repository.JpaRepository

interface NoteGroupRepository : JpaRepository<NoteGroup, Long> {
    fun existsByOriginalName(originalName: String): Boolean

    fun findByOriginalName(originalName: String): NoteGroup?

    fun findNoteGroupById(id: Long): NoteGroup?
}