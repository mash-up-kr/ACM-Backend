package mashup.backend.spring.acm.collector.brand.rename

import mashup.backend.spring.acm.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = BrandRenameConfig.JOB_NAME
)
@Configuration
class BrandRenameConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
) {
    @Bean
    fun brandRenameJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(brandRenameTasklet())
                    .transactionManager(resourcelessTransactionManager)
                    .build()
            )
            .build()
    }

    @Bean
    fun brandRenameTasklet(): Tasklet = BrandRenameTasklet()

    companion object {
        const val JOB_NAME = "brandRenameJob"
        const val STEP_NAME = "brandRenameStep"
    }
}