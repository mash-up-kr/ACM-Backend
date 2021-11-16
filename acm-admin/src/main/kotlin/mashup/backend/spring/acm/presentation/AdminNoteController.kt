package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.note.NoteService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/note")
class AdminNoteController(
    private val noteService: NoteService
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val notePage = noteService.getNotes(pageable)
        model.addAttribute("notePage", notePage)
        return "note/list"
    }
}