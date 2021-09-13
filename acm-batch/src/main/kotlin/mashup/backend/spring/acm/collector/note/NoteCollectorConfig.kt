package mashup.backend.spring.acm.collector.note

import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// TODO: ConditionalOnProperty
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
        return jobBuilderFactory["noteCollectorJob"]
            .repository(jobRepository)
            .start(
                stepBuilderFactory["noteCollectorStep"]
                    .tasklet(noteCollectorTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    fun noteCollectorTasklet(): Tasklet = NoteCollectorTasklet()
}