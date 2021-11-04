package mashup.backend.spring.acm.domain.note

data class NoteGroupDetailVo(
    val id: Long,
    val name: String,
    val customName: String?,
    val description: String,
    val notes: List<NoteSimpleVo>,
) {
    constructor(noteGroup: NoteGroup) : this(
        id = noteGroup.id,
        name = noteGroup.name,
        customName = noteGroup.customName,
        description = noteGroup.description,
        notes = noteGroup.notes.map { NoteSimpleVo(it) }
    )
}