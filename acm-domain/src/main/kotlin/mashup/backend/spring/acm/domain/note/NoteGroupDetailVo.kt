package mashup.backend.spring.acm.domain.note

data class NoteGroupDetailVo(
    val id: Long,
    val name: String,
    val description: String,
    val notes: List<NoteSimpleVo>,
) {
    constructor(noteGroup: NoteGroup) : this(
        id = noteGroup.id,
        name = noteGroup.name,
        description = noteGroup.description,
        notes = noteGroup.notes.map { NoteSimpleVo(it) }
    )
}