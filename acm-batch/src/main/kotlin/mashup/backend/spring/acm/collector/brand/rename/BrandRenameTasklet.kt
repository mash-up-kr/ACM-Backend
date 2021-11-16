package mashup.backend.spring.acm.collector.brand.rename

import mashup.backend.spring.acm.domain.brand.BrandService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

/**
 * 브랜드 이름에 포함된 ' perfumes and colognes' 문자열 삭제
 */
open class BrandRenameTasklet : Tasklet {
    @Autowired
    lateinit var brandService: BrandService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        brandService.findAll().forEach() { brand ->
            val beforeName = brand.name
            val afterName = beforeName.replace(" perfumes and colognes", "")
            brandService.rename(
                brandId = brand.id,
                name = afterName
            )
            log.info("before: $beforeName, after: $afterName")
        }
        return RepeatStatus.FINISHED
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BrandRenameTasklet::class.java)
    }
}