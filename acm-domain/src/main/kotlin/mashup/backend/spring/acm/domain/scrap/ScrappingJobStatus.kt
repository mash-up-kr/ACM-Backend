package mashup.backend.spring.acm.domain.scrap

enum class ScrappingJobStatus(
    private val description: String
) {
    WAITING("대기"),
    SUCCESS("성공"),
    FAILURE("실패"),
    PROCESSING("처리중"),
}