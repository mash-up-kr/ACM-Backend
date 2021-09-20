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
        if (perfumeUrlRepository.existsByUrl(perfumeUrl)) {
            return Pair(false, null)
        }
        val perfumeUrlScrapingJob = perfumeUrlRepository.save(PerfumeUrlScrapingJob(url = perfumeUrl))
        return Pair(true, perfumeUrlScrapingJob)
    }
}