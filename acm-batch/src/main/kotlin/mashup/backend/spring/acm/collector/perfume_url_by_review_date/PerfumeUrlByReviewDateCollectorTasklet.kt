package mashup.backend.spring.acm.collector.perfume_url_by_review_date

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate
import java.time.YearMonth

/**
 * review 작성된 날짜를 기준으로 향수 목록을 저장함
 */
open class PerfumeUrlByReviewDateCollectorTasklet(
    private val perfumeUrlByReviewDateScrapingService: PerfumeUrlByReviewDateScrapingService,
) : Tasklet {
    @Value("#{jobParameters[reviewDate]}")
    lateinit var reviewDateString: String

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val (firstDate, lastDate) = resolveReviewDate()
        var reviewDate = firstDate
        while (!reviewDate.isAfter(lastDate)) {
            perfumeUrlByReviewDateScrapingService.scrap(reviewDate)
            reviewDate = reviewDate.plusDays(1L)
            try {
                Thread.sleep(60000)
            } catch (e: InterruptedException) {
                // ignore
            }
        }

        return RepeatStatus.FINISHED
    }

    private fun resolveReviewDate(): Pair<LocalDate, LocalDate> {
        return if (REGEX_YEAR_MONTH.matches(reviewDateString)) {
            val yearMonth = YearMonth.parse(reviewDateString)
            Pair(yearMonth.atDay(1), yearMonth.atEndOfMonth())
        } else {
            val reviewDate = LocalDate.parse(reviewDateString)
            Pair(reviewDate, reviewDate)
        }
    }

    companion object {
        val REGEX_YEAR_MONTH = "\\d{4}-\\d{2}".toRegex()
    }
}