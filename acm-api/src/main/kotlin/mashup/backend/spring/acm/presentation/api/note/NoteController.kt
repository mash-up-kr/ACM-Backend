package mashup.backend.spring.acm.presentation.api.note

import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notes")
class NoteController {
    @GetMapping("/{noteId}")
    fun getNoteDetail(noteId: Long): ApiResponse<NoteDetailData> = TODO()
}
