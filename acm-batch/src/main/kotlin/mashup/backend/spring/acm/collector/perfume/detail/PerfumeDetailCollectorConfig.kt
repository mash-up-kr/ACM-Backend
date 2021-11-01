package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = PerfumeDetailCollectorConfig.JOB_NAME
)
@Configuration
class PerfumeDetailCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
) {
    @Bean
    fun perfumeDetailCollectorJob(): Job {
        return jobBuilderFactory[JOB_NAME]
            .repository(jobRepository)
            .start(
                stepBuilderFactory[STEP_NAME]
                    .tasklet(perfumeDetailCollectorTasklet())
                    .transactionManager(resourcelessTransactionManager)
                    .build()
            )
            .build()
    }

    @Bean
    @StepScope
    fun perfumeDetailCollectorTasklet() = PerfumeDetailCollectorTasklet()

    @Bean
    fun perfumeNoteMappingService() = PerfumeNoteMappingService()

    @Bean
    fun perfumeBrandMappingService() = PerfumeBrandMappingService()

    companion object {
        const val JOB_NAME = "perfumeDetailCollectorJob"
        const val STEP_NAME = "perfumeDetailCollectorStep"
    }
}