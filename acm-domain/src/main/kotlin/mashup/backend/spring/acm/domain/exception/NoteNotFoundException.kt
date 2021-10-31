package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class NoteNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.NOTE_NOT_FOUND,
    message = message,
    cause = cause
) {
    constructor(noteId: Long) : this(
        message = "노트를 찾을 수 없습니다. noteId: $noteId",
    )
}
