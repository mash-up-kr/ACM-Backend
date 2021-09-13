package mashup.backend.spring.acm.collector.mapping

data class SimplePerfume(
    val perfumeId: String? = "",
    val name: String = "",
    val topNotes: List<SimpleNote> = emptyList(),
    val middleNotes: List<SimpleNote> = emptyList(),
    val baseNotes: List<SimpleNote> = emptyList(),
    val otherNotes: List<SimpleNote> = emptyList()
)