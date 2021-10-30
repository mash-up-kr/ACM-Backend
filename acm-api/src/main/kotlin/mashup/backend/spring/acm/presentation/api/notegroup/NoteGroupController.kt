package mashup.backend.spring.acm.presentation.api.notegroup

import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/note-groups")
class NoteGroupController {
    @GetMapping
    fun getNoteGroupList(): ApiResponse<NoteGroupListResponse> = TODO()
}