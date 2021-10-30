package mashup.backend.spring.acm.presentation.api.notegroup

data class NoteGroupListResponse(
    val noteGroups: List<NoteGroupSimpleResponse>
)

data class NoteGroupSimpleResponse(
    val id: Long,
    val name: String,
)

