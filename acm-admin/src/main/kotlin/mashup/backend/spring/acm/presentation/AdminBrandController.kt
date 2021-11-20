package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.brand.BrandService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/brand")
class AdminBrandController(
    private val brandService: BrandService,
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val brandSimpleVoPage = brandService.getBrands(pageable)
        model.addAttribute("brandSimpleVoPage", brandSimpleVoPage)
        return "brand/list"
    }
}