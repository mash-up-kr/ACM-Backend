package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.note.Note
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ManyToOne

@Entity
class PerfumeNote(
    @ManyToOne
    val perfume: Perfume,
    @ManyToOne
    val note: Note,
    @Enumerated(EnumType.STRING)
    var noteType: PerfumeNoteType
) : BaseEntity() {
}