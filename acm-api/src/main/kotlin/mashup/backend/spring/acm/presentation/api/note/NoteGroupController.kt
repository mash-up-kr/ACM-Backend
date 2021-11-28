package mashup.backend.spring.acm.presentation.api.note

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.note.NoteGroupApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import mashup.backend.spring.acm.presentation.assembler.toDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(
    description = "노트그룹",
    tags = ["note-group"]
)
@RestController
@RequestMapping("/api/v1/note-groups")
class NoteGroupController(
    private val noteGroupApplicationService: NoteGroupApplicationService,
) {
    @ApiOperation(
        value = "노트 그룹 목록 조회",
    )
    @GetMapping
    fun getNoteGroupList(): ApiResponse<NoteGroupListData> {
        return ApiResponse.success(
            data = NoteGroupListData(
                noteGroups = noteGroupApplicationService.getAllNoteGroups().map { it.toDto() }
            )
        )
    }

    @ApiOperation(
        value = "노트 그룹 상세 조회",
    )
    @GetMapping("/{noteGroupId}")
    fun getNoteGroup(
        @PathVariable noteGroupId: Long,
    ): ApiResponse<NoteGroupDetailData> {
        return ApiResponse.success(
            data = NoteGroupDetailData(
                noteGroup = noteGroupApplicationService.getNoteGroup(noteGroupId = noteGroupId).toDto(),
            ),
        )
    }
}