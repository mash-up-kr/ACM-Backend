package mashup.backend.spring.acm.domain.note

data class NoteDetailVo(
    val id: Long,
    val name: String,
    var description: String,
    val thumbnailImageUrl: String,
    var noteGroup: NoteGroupSimpleVo?,
) {
    constructor(note: Note) : this(
        id = note.id,
        name = note.name,
        description = note.description,
        thumbnailImageUrl = note.thumbnailImageUrl,
        noteGroup = note.noteGroup?.let { NoteGroupSimpleVo(it) }
    )
}
