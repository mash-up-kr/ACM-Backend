package mashup.backend.spring.acm.collector.note.detail

import mashup.backend.spring.acm.collector.note.NoteCollectorConfig
import mashup.backend.spring.acm.collector.note.NoteCollectorTasklet
import mashup.backend.spring.acm.infrastructure.BatchConfig
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
    name = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = NoteDetailCollectorConfig.JOB_NAME
)
@Configuration
class NoteDetailCollectorConfig {

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun noteDetailCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(noteDetailCollectorTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    fun noteDetailCollectorTasklet(): Tasklet = NoteDetailCollectorTasklet()

    companion object {
        const val JOB_NAME = "noteDetailCollectorJob"
        const val STEP_NAME = "noteDetailCollectorStep"
    }
}