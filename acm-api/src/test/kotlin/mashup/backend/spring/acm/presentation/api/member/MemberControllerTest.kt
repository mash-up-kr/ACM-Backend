package mashup.backend.spring.acm.presentation.api.member

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import mashup.backend.spring.acm.application.member.MemberApplicationService
import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberGender
import mashup.backend.spring.acm.domain.member.MemberStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @MockkBean
    lateinit var memberApplicationService: MemberApplicationService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun memberMe() {
        // given
        val memberId = 1L
        every { memberApplicationService.getMemberInfo(any()) } returns MemberDetailVo(
            id = memberId,
            status = MemberStatus.ACTIVE,
            name = "도비",
            gender = MemberGender.MALE,
            ageGroup = AgeGroup.THIRTIES,
            noteGroupIds = listOf(),
            noteGroupSimpleVoList = listOf(),
        )
        // when
        mockMvc.perform(get("/api/v1/members/me")
            .with(user(memberId.toString()).authorities(SimpleGrantedAuthority("MEMBER"))))
            // then
            .andExpect(status().isOk)
    }
}