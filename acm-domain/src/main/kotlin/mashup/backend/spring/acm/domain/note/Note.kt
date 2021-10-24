package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Note(
    val name: String,
    val description: String = "",
    val url: String,
    val thumbnailImageUrl: String,
    @ManyToOne
    var noteGroup: NoteGroup?
) : BaseEntity() {
    companion object {
        fun of(noteCreateVo: NoteCreateVo, noteGroup: NoteGroup) = Note(
            name = noteCreateVo.name,
            description = noteCreateVo.description,
            url = noteCreateVo.url,
            thumbnailImageUrl = noteCreateVo.thumbnailImageUrl,
            noteGroup = null
        )
    }
}