package mashup.backend.spring.acm.collector.perfume.detail

data class SimplePerfume(
    val perfumeId: String? = "",
    val name: String = "",
    val url: String,
    val topNotes: List<SimpleNote> = emptyList(),
    val middleNotes: List<SimpleNote> = emptyList(),
    val baseNotes: List<SimpleNote> = emptyList(),
    val otherNotes: List<SimpleNote> = emptyList()
)