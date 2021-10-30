package mashup.backend.spring.acm.presentation.api.note

data class NoteGroupListResponse(
    val noteGroups: List<NoteGroupSimpleResponse>,
)

data class NoteGroupSimpleResponse(
    val id: Long,
    val name: String,
)

