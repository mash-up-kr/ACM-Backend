package mashup.backend.spring.acm.domain.note

class DuplicatedNoteException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()
