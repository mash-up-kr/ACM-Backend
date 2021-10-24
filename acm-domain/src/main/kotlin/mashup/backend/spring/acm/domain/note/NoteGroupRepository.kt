package mashup.backend.spring.acm.domain.note

import org.springframework.data.jpa.repository.JpaRepository

interface NoteGroupRepository : JpaRepository<NoteGroup, Long> {
    fun existsByOriginalName(originalName: String): Boolean
}