package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class DuplicatedNoteException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : BadRequestException(
    resultCode = ResultCode.NOTE_ALREADY_EXIST,
    message = message,
    cause = cause,
)
