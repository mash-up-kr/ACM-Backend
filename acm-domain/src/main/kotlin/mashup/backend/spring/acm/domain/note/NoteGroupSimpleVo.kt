package mashup.backend.spring.acm.domain.note

data class NoteGroupSimpleVo(
    val id: Long,
    val name: String,
) {
    constructor(noteGroup: NoteGroup): this(
        id = noteGroup.id,
        name = noteGroup.name,
    )
}