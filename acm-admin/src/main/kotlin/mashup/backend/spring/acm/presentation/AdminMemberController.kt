package mashup.backend.spring.acm.presentation

import mashup.backend.spring.acm.domain.member.MemberService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/member")
class AdminMemberController(
    private val memberService: MemberService,
) {
    @GetMapping
    fun list(
        pageable: Pageable,
        model: Model,
    ): String {
        val memberDetailVoPage = memberService.getMembers(pageable)
        model.addAttribute("memberDetailVoPage", memberDetailVoPage)
        return "member/list"
    }
}