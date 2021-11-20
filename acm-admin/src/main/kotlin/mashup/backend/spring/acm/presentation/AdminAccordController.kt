package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.accord.AccordService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/accord")
class AdminAccordController(
    private val accordService: AccordService
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val accordPage = accordService.getAccords(pageable)
        model.addAttribute("accordPage", accordPage)
        return "accord/list"
    }
}