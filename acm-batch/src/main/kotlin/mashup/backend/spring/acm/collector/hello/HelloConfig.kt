package mashup.backend.spring.acm.collector.hello

import mashup.backend.spring.acm.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = HelloConfig.JOB_NAME
)
@Component
class HelloConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun helloJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(helloTasklet())
                    .build()
            )
            .build()
    }

    @Bean
    @StepScope
    fun helloTasklet(): Tasklet = HelloTasklet()

    companion object {
        const val JOB_NAME = "helloJob"
        const val STEP_NAME = "helloStep"
    }
}