package mashup.backend.spring.acm.collector.notegroup

import mashup.backend.spring.acm.BatchConfig.Companion.SPRING_BATCH_JOB_NAMES
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = [SPRING_BATCH_JOB_NAMES],
    havingValue = NoteGroupCollectorConfig.JOB_NAME
)
@Configuration
class NoteGroupCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun noteGroupCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(noteGroupCollectorTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    @StepScope
    fun noteGroupCollectorTasklet() = NoteGroupCollectorTasklet()

    companion object {
        const val JOB_NAME = "noteGroupCollectorJob"
        const val STEP_NAME = "noteGroupCollectorStep"
    }
}