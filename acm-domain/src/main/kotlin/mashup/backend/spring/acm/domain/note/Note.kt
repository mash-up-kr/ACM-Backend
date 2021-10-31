package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Note(
    val name: String,
    var description: String = "",
    val url: String,
    val thumbnailImageUrl: String,
    @ManyToOne
    @JoinColumn
    var noteGroup: NoteGroup?
) : BaseEntity() {
    companion object {
        fun from(noteCreateVo: NoteCreateVo) = Note(
            name = noteCreateVo.name,
            description = noteCreateVo.description,
            url = noteCreateVo.url,
            thumbnailImageUrl = noteCreateVo.thumbnailImageUrl,
            noteGroup = null
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (name != other.name) return false
        if (url != other.url) return false
        if (thumbnailImageUrl != other.thumbnailImageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + thumbnailImageUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "Note(name='$name', description='${description.take(30)}', url='$url', thumbnailImageUrl='$thumbnailImageUrl', noteGroup=$noteGroup)"
    }
}