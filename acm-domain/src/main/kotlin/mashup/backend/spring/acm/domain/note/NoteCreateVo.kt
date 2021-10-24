package mashup.backend.spring.acm.domain.note

data class NoteCreateVo(
    val name: String,
    val description: String,
    val url: String,
    val thumbnailImageUrl: String,
    val noteGroupName: String,
)