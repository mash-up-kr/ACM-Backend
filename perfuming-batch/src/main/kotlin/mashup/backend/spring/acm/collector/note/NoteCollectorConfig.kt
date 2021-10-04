package mashup.backend.spring.acm.collector.note

import mashup.backend.spring.acm.BatchConfig.Companion.SPRING_BATCH_JOB_NAMES
import mashup.backend.spring.acm.collector.notegroup.NoteGroupCollectorConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = [SPRING_BATCH_JOB_NAMES],
    havingValue = NoteCollectorConfig.JOB_NAME
)
@Configuration
class NoteCollectorConfig {
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory
    @Autowired
    lateinit var jobRepository: JobRepository
    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun noteCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(noteCollectorTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    fun noteCollectorTasklet(): Tasklet = NoteCollectorTasklet()

    companion object {
        const val JOB_NAME = "noteCollectorJob"
        const val STEP_NAME = "noteCollectorStep"
    }
}