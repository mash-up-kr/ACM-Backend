package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.perfume.PerfumeService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/perfume")
class AdminPerfumeController(
    private val perfumeService: PerfumeService
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val perfumeSimpleVoPage = perfumeService.getPerfumes(pageable)
        model.addAttribute("perfumeSimpleVoPage", perfumeSimpleVoPage)
        return "perfume/list"
    }
}