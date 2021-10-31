package mashup.backend.spring.acm.domain.note

data class NoteSimpleVo(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailImageUrl: String,
) {
    constructor(note: Note) : this(
        id = note.id,
        name = note.name,
        description = note.description,
        thumbnailImageUrl = note.thumbnailImageUrl,
    )
}