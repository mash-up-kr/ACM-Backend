package mashup.backend.spring.acm.collector.mapping

import mashup.backend.spring.acm.infrastructure.BatchConfig.Companion.SPRING_BATCH_JOB_NAMES
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
    havingValue = PerfumeNoteMapCollectorConfig.JOB_NAME
)
@Configuration
class PerfumeNoteMapCollectorConfig {
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun perfumeNoteMapCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(perfumeNoteMapCollectorTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    fun perfumeNoteMapCollectorTasklet(): Tasklet = PerfumeNoteMapCollectorTasklet()

    companion object {
        const val JOB_NAME = "perfumeNoteMapCollectorJob"
        const val STEP_NAME = "perfumeNoteMapCollectorStep"
    }
}