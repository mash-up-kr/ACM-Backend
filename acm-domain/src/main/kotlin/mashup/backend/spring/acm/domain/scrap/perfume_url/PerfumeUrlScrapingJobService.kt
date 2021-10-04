package mashup.backend.spring.acm.domain.scrap.perfume_url

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PerfumeUrlScrapingJobService(
    private val perfumeUrlRepository: PerfumeUrlRepository
) {
    @Transactional
    fun createIfNotExists(perfumeUrl: String) : Pair<Boolean, PerfumeUrlScrapingJob?> {
        val url = if (perfumeUrl.startsWith("https://www.fragrantica.com/")) {
            perfumeUrl
        } else {
            "https://www.fragrantica.com$perfumeUrl"
        }
        if (perfumeUrlRepository.existsByUrl(url)) {
            return Pair(false, null)
        }
        val perfumeUrlScrapingJob = perfumeUrlRepository.save(PerfumeUrlScrapingJob(url))
        return Pair(true, perfumeUrlScrapingJob)
    }
}