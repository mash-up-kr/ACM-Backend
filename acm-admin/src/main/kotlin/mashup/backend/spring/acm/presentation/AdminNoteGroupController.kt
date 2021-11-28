package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.note.NoteGroupService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/note-group")
class AdminNoteGroupController(
    private val noteGroupService: NoteGroupService
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val noteGroupPage = noteGroupService.getNoteGroups(pageable)
        model.addAttribute("noteGroupPage", noteGroupPage)
        return "note-group/list"
    }
}