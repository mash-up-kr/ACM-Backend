package mashup.backend.spring.acm.infrastructure

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.context.annotation.Configuration

/**
 * 암호화, 복호화 관련 설정
 * @see <a href="https://github.com/ulisesbocchio/jasypt-spring-boot">https://github.com/ulisesbocchio/jasypt-spring-boot</a>
 */
@Configuration
@EnableEncryptableProperties
class JasyptConfig {
}