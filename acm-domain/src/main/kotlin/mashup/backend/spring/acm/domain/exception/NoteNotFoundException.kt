package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class NoteNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.NOTE_NOT_FOUND,
    message = message,
    cause = cause
)
