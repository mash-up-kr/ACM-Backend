package mashup.backend.spring.acm.domain.scrap

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class ScrapingJob(
    open var status: ScrappingJobStatus
) : BaseEntity()
