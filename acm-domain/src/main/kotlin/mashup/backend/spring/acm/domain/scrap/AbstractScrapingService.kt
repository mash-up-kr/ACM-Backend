package mashup.backend.spring.acm.domain.scrap

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractScrapingService<JOB : ScrapingJob, REQUEST> {
    fun scrap(request: REQUEST) {
        val scrapingJob: JOB?
        try {
            scrapingJob = preProcess(request)
        } catch (e: Exception) {
            log.error("Failed to preProcess", e)
            return
        }
        val isSuccess = try {
            process(request)
            true
        } catch (e: Exception) {
            false
        }
        postProcess(isSuccess, scrapingJob)
    }

    protected open fun preProcess(request: REQUEST): JOB? {
        return null
    }

    protected open fun process(request: REQUEST) {
    }

    protected open fun postProcess(isSuccess: Boolean, scrapingJob: JOB?) {
    }

    companion object {
        val log : Logger = LoggerFactory.getLogger(this::class.java)
    }
}