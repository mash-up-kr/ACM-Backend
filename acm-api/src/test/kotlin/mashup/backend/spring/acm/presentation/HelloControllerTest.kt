package mashup.backend.spring.acm.presentation

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
internal class HelloControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun hello() {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk)
    }
}