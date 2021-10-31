package mashup.backend.spring.acm.presentation.api.note

import mashup.backend.spring.acm.application.note.NoteApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notes")
class NoteController(
    private val noteApplicationService: NoteApplicationService,
) {
    /**
     * 노트 상세 조회
     */
    @GetMapping("/{noteId}")
    fun getNoteDetail(noteId: Long): ApiResponse<NoteDetailData> {
        return ApiResponse.success(
            data = NoteDetailData(
                note = noteApplicationService.getNote(noteId = noteId),
            ),
        )
    }
}
