package mashup.backend.spring.acm.domain.scrap

enum class ScrappingJobStatus(
    private val description: String
) {
    PROCESSING("처리중"),
    SUCCESS("성공"),
    FAILURE("실패"),
}