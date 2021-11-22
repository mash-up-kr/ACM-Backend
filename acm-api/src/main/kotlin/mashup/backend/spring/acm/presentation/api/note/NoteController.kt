package mashup.backend.spring.acm.presentation.api.note

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.note.NoteApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(
    description = "노트",
    tags = ["note"],
)
@RestController
@RequestMapping("/api/v1/notes")
class NoteController(
    private val noteApplicationService: NoteApplicationService,
) {
    @ApiOperation(
        value = "노트 상세 조회",
    )
    @GetMapping("/{noteId}")
    fun getNoteDetail(@PathVariable noteId: Long): ApiResponse<NoteDetailData> {
        return ApiResponse.success(
            data = NoteDetailData(
                note = noteApplicationService.getNote(noteId = noteId),
            ),
        )
    }
}
