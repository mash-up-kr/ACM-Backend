package mashup.backend.spring.acm.presentation.api.note

import mashup.backend.spring.acm.presentation.api.perfume.PerfumeSimpleResponse

data class NoteDetailData(
    val note: NoteDetailResponse,
)

data class NoteDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val thumbnailImageUrl: String,
    val noteGroup: NoteGroupSimpleResponse,
    val perfumes: List<PerfumeSimpleResponse>,
)

data class NoteGroupSimpleResponse(
    val id: Long,
    val name: String,
)
