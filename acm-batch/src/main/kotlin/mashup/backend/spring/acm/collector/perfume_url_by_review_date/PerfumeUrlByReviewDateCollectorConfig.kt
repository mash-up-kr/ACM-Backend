package mashup.backend.spring.acm.collector.perfume_url_by_review_date

import mashup.backend.spring.acm.domain.scrap.perfume_by_rewiew_date.PerfumeReviewScrapingJobService
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
    havingValue = PerfumeUrlByReviewDateCollectorConfig.JOB_NAME
)
@Configuration
class PerfumeUrlByReviewDateCollectorConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository,
    private val stepBuilderFactory: StepBuilderFactory,
    private val resourcelessTransactionManager: ResourcelessTransactionManager,
    private val perfumeReviewScrapingJobService: PerfumeReviewScrapingJobService,
    private val perfumeUrlScrapingJobService: PerfumeUrlScrapingJobService
) {

    @Bean
    fun perfumeReviewDateCollectorJob(): Job = jobBuilderFactory[JOB_NAME]
        .repository(jobRepository)
        .start(perfumeReviewDateCollectorStep())
        .build()

    @Bean
    @JobScope
    fun perfumeReviewDateCollectorStep(): Step = stepBuilderFactory[STEP_NAME]
        .tasklet(perfumeUrlByReviewDateCollectorTasklet())
        .transactionManager(resourcelessTransactionManager)
        .build()

    @Bean
    @StepScope
    fun perfumeUrlByReviewDateCollectorTasklet() = PerfumeUrlByReviewDateCollectorTasklet(
        perfumeUrlByReviewDateScrapingService = perfumeUrlByReviewDateScrapingService()
    )

    @Bean
    fun perfumeUrlByReviewDateScrapingService() = PerfumeUrlByReviewDateScrapingService(
        perfumeReviewScrapingJobService = perfumeReviewScrapingJobService,
        perfumeUrlScrapingJobService = perfumeUrlScrapingJobService
    )

    companion object {
        const val JOB_NAME = "perfumeUrlByReviewDateCollectorJob"
        const val STEP_NAME = "perfumeUrlByReviewDateCollectorStep"
    }
}