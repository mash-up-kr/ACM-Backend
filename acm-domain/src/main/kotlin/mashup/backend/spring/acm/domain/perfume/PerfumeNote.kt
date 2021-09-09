package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.note.Note
import javax.persistence.*

@Entity
class PerfumeNote(
    @ManyToOne
    @JoinColumn(name = "perfumeId")
    val perfume: Perfume,
    @ManyToOne
    @JoinColumn(name = "noteId")
    val note: Note,
    @Enumerated(EnumType.STRING)
    var noteType: PerfumeNoteType
) : BaseEntity() {
}