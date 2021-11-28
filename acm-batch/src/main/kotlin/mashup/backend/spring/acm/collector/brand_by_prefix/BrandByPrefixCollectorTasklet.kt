package mashup.backend.spring.acm.collector.brand_by_prefix

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value

/**
 * 브랜드 이름의 첫글자로 구분해서 브랜드 url 목록을 크롤링
 * @see <a href=https://www.fragrantica.com/designers-1/>https://www.fragrantica.com/designers-1</a>
 */
open class BrandByPrefixCollectorTasklet(
    private val brandUrlByPrefixScrapingService: BrandUrlByPrefixScrapingService,
) : Tasklet {
    // 1 ~ 11
    @Value("#{jobParameters[prefix]}")
    lateinit var prefix: String

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        brandUrlByPrefixScrapingService.scrap(request = prefix)
        return RepeatStatus.FINISHED
    }
}