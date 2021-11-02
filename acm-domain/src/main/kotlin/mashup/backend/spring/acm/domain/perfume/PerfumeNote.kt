package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.note.Note
import javax.persistence.*

@Entity
class PerfumeNote(
    @ManyToOne
    @JoinColumn
    val perfume: Perfume,
    @ManyToOne
    @JoinColumn
    val note: Note,
    @Enumerated(EnumType.STRING)
    var noteType: PerfumeNoteType
) : BaseEntity() {
}