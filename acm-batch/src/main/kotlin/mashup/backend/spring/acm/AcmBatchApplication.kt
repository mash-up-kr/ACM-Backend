package mashup.backend.spring.acm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime

@SpringBootApplication
class AcmBatchApplication

fun main(args: Array<String>) {
	System.setProperty("spring.config.name", "application,batch")
	val argsWithStartTime = args + " startTime=${LocalDateTime.now()}"
	runApplication<AcmBatchApplication>(*argsWithStartTime)
}
