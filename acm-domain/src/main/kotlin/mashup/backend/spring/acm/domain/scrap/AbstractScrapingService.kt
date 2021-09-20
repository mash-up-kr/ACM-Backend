package mashup.backend.spring.acm.domain.scrap

abstract class AbstractScrapingService<JOB : ScrapingJob, REQUEST> {
    fun scrap(request: REQUEST) {
        val scrapingJob = preProcess(request)
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
}