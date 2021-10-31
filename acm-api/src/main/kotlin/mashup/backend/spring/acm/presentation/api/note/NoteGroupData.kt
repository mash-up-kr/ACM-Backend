package mashup.backend.spring.acm.presentation.api.note

data class NoteGroupListData(
    val noteGroups: List<NoteGroupSimpleResponse>,
)

data class NoteGroupSimpleResponse(
    val id: Long,
    val name: String,
)

data class NoteGroupDetailData(
    val noteGroup: NoteGroupDetailResponse
)

data class NoteGroupDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val notes: List<NoteSimpleResponse>,
)
