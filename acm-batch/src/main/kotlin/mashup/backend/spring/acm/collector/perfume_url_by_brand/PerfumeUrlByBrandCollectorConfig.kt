package mashup.backend.spring.acm.collector.perfume_url_by_brand

import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.scrap.perfume_by_brand.PerfumeBrandScrapingJobService
import mashup.backend.spring.acm.domain.scrap.perfume_url.PerfumeUrlScrapingJobService
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
    havingValue = PerfumeUrlByBrandCollectorConfig.JOB_NAME
)
@Configuration
class PerfumeUrlByBrandCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
    private val perfumeBrandScrapingJobService: PerfumeBrandScrapingJobService,
    private val perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService,
    private val brandService: BrandService,
) {

    @Bean
    fun perfumeUrlByBrandCollectorJob(): Job = jobBuilderFactory[JOB_NAME]
        .repository(jobRepository)
        .start(perfumeUrlByBrandCollectorStep())
        .build()

    @Bean
    @JobScope
    fun perfumeUrlByBrandCollectorStep(): Step = stepBuilderFactory[STEP_NAME]
        .tasklet(perfumeUrlByBrandCollectorTasklet())
        .transactionManager(resourcelessTransactionManager)
        .build()

    @Bean
    @StepScope
    fun perfumeUrlByBrandCollectorTasklet() = PerfumeUrlByBrandCollectorTasklet(
        perfumeUrlByBrandScrapingService = perfumeUrlByBrandScrapingService(),
        perfumeBrandScrapingJobService = perfumeBrandScrapingJobService,
        brandService = brandService,
    )

    @Bean
    fun perfumeUrlByBrandScrapingService() = PerfumeUrlByBrandScrapingService(
        perfumeBrandScrapingJobService = perfumeBrandScrapingJobService,
        perfumeUrlScrapingJobService = perfumeUrlScrapingJobService,
    )

    companion object {
        const val JOB_NAME = "perfumeUrlByBrandCollectorJob"
        const val STEP_NAME = "perfumeUrlByBrandCollectorStep"
    }
}