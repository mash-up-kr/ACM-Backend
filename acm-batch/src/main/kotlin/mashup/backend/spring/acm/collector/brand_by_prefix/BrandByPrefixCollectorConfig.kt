package mashup.backend.spring.acm.collector.brand_by_prefix

import mashup.backend.spring.acm.domain.scrap.brand_url.BrandPrefixScrapingJobService
import mashup.backend.spring.acm.domain.scrap.brand_url.BrandUrlScrapingJobService
import mashup.backend.spring.acm.infrastructure.BatchConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    value = [BatchConfig.SPRING_BATCH_JOB_NAMES],
    havingValue = BrandByPrefixCollectorConfig.JOB_NAME
)
@Configuration
class BrandByPrefixCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
    private val brandUrlScrapingJobService: BrandUrlScrapingJobService,
    private val brandPrefixScrapingJobService: BrandPrefixScrapingJobService,
) {
    @Bean
    fun brandUrlByPrefixCollectorJob(): Job = jobBuilderFactory[JOB_NAME]
        .repository(jobRepository)
        .start(brandUrlByPrefixCollectorStep())
        .build()

    @Bean
    @JobScope
    fun brandUrlByPrefixCollectorStep(): Step = stepBuilderFactory[STEP_NAME]
        .tasklet(brandUrlByPrefixCollectorTasklet())
        .transactionManager(resourcelessTransactionManager)
        .build()

    @Bean
    @StepScope
    fun brandUrlByPrefixCollectorTasklet() = BrandByPrefixCollectorTasklet(
        brandUrlByPrefixScrapingService = brandUrlByPrefixScrapingService()
    )

    @Bean
    fun brandUrlByPrefixScrapingService() = BrandUrlByPrefixScrapingService(
        brandPrefixScrapingJobService = brandPrefixScrapingJobService,
        brandUrlScrapingJobService = brandUrlScrapingJobService,
    )

    companion object {
        const val JOB_NAME = "brandUrlByPrefixCollectorJob"
        const val STEP_NAME = "brandUrlByPrefixCollectorStep"
    }
}