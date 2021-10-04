package mashup.backend.spring.acm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AcmAdminApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application,admin")
    runApplication<AcmAdminApplication>(*args)
}
