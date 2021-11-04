package mashup.backend.spring.acm.collector.brand.detail

import mashup.backend.spring.acm.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = BrandDetailCollectorConfig.JOB_NAME
)
@Configuration
class BrandDetailCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
) {
    @Bean
    fun brandDetailCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(brandDetailCollectorStep())
            .build()
    }

    @Bean
    @JobScope
    fun brandDetailCollectorStep(): Step = stepBuilderFactory[STEP_NAME]
        .tasklet(brandDetailCollectorTasklet())
        .transactionManager(resourcelessTransactionManager)
        .build()

    @Bean
    @StepScope
    fun brandDetailCollectorTasklet(): Tasklet = BrandDetailCollectorTasklet()

    @Bean
    fun brandDetailParser() = BrandDetailParser()

    companion object {
        const val JOB_NAME = "brandDetailCollectorJob"
        const val STEP_NAME = "brandDetailCollectorStep"
    }
}