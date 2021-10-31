package mashup.backend.spring.acm.domain.exception

import mashup.backend.spring.acm.domain.ResultCode

class NoteGroupNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : NotFoundException(
    resultCode = ResultCode.NOTE_GROUP_NOT_FOUND,
    message = message,
    cause = cause,
) {
    constructor(noteGroupId: Long) : this(
        message = "노트 그룹을 찾을 수 없습니다. noteGroupId: $noteGroupId",
    )
}
