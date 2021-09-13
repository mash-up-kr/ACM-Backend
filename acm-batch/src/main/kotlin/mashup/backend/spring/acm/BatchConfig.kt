package mashup.backend.spring.acm

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class BatchConfig {
    companion object {
        const val SPRING_BATCH_JOB_NAMES = "spring.batch.job.names"
    }
}