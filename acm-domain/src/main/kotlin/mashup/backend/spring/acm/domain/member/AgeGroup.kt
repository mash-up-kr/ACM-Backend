package mashup.backend.spring.acm.domain.member

enum class AgeGroup(private val description: String) {
    TEENAGER("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    UNKNOWN("모름"),
    ;
}