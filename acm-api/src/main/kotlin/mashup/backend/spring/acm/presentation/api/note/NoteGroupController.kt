package mashup.backend.spring.acm.presentation.api.note

import mashup.backend.spring.acm.application.note.NoteApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun getNoteGroupList(): ApiResponse<NoteGroupListData> {
        return ApiResponse.success(
            data = NoteGroupListData(
                noteGroups = noteApplicationService.getAllNoteGroups().map { it.toDto() }
            )
        )
    }

    /**
     * 노트 그룹 상세 조회
     */
    @GetMapping("/{noteGroupId}")
    fun getNoteGroup(
        @PathVariable noteGroupId: Long,
    ): ApiResponse<NoteGroupDetailData> {
        return ApiResponse.success(
            data = NoteGroupDetailData(
                noteGroup = noteApplicationService.getNoteGroup(noteGroupId = noteGroupId).toDto(),
            ),
        )
    }
}