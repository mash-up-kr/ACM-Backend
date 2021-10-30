package mashup.backend.spring.acm.presentation.api.note

import mashup.backend.spring.acm.application.note.NoteApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/note-groups")
class NoteGroupController(
    private val noteApplicationService: NoteApplicationService,
) {
    /**
     * 노트 그룹 목록 조회
     */
    @GetMapping
    fun getNoteGroupList(): ApiResponse<NoteGroupListResponse> {
        return ApiResponse.success(
            data = NoteGroupListResponse(
                noteGroups = noteApplicationService.getAllNoteGroups().map { it.toDto() }
            )
        )
    }
}