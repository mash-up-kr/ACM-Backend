package mashup.backend.spring.acm.infrastructure

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()
        val caches = CacheType.values()
            .map { cache ->
                CaffeineCache(
                    cache.cacheName,
                    Caffeine.newBuilder()
                        .expireAfterWrite(cache.expiredAfterWrite, TimeUnit.SECONDS)
                        .maximumSize(cache.maximumSize)
                        .build()
                )
            }
        cacheManager.setCaches(caches)

        return cacheManager
    }
}

enum class CacheType(
    val cacheName: String,
    val expiredAfterWrite: Long, // second
    val maximumSize: Long
) {
    // note group
    POPULAR_NOTE_GROUP(CacheNames.POPULAR_NOTE_GROUP, CacheExpiredAfterWrite.EXPIRE_1_DAY, 1),

    // perfumes
    PERFUMES_BY_NOTE_ID(CacheNames.PERFUMES_BY_NOTE_ID, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10000),
    RECOMMEND_MAIN_PERFUMES(CacheNames.RECOMMEND_MAIN_PERFUMES, CacheExpiredAfterWrite.EXPIRE_60_SEC, 10000),
    RECOMMEND_GENDER_PERFUMES(CacheNames.RECOMMEND_GENDER_PERFUMES, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10),
    RECOMMEND_POPULAR_PERFUMES(CacheNames.RECOMMEND_POPULAR_PERFUMES, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10),
    RECOMMEND_MONTHLY_PERFUMES(CacheNames.RECOMMEND_MONTHLY_PERFUMES, CacheExpiredAfterWrite.EXPIRE_FOREVER, 10),
    RECOMMEND_DEFAULT_PERFUMES(CacheNames.RECOMMEND_DEFAULT_PERFUMES, CacheExpiredAfterWrite.EXPIRE_FOREVER, 10),
    RECOMMEND_PRESENT_PERFUMES(CacheNames.RECOMMEND_PRESENT_PERFUMES, CacheExpiredAfterWrite.EXPIRE_FOREVER, 10),

    // recommend notes
    RECOMMEND_DEFAULT_NOTES(CacheNames.RECOMMEND_DEFAULT_NOTES, CacheExpiredAfterWrite.EXPIRE_FOREVER, 10),

    // brand
    POPULAR_BRANDS(CacheNames.POPULAR_BRANDS, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10);

    object CacheNames {
        // note group
        const val POPULAR_NOTE_GROUP = "popularNoteGroup"

        // perfumes
        const val PERFUMES_BY_NOTE_ID = "perfumesByNoteId"

        // recommend perfumes
        const val RECOMMEND_MAIN_PERFUMES = "recommendMainPerfumes"
        const val RECOMMEND_GENDER_PERFUMES = "recommendGenderPerfumes"
        const val RECOMMEND_POPULAR_PERFUMES = "recommendPopularPerfumes"
        const val RECOMMEND_MONTHLY_PERFUMES = "recommendMonthlyPerfumes"
        const val RECOMMEND_DEFAULT_PERFUMES = "recommendDefaultPerfumes"
        const val RECOMMEND_PRESENT_PERFUMES = "recommendPresentPerfumes"

        // recommend notes
        const val RECOMMEND_DEFAULT_NOTES = "recommendDefaultNotes"

        // brands
        const val POPULAR_BRANDS = "popularBrands"
    }

    // 단위는 second
    object CacheExpiredAfterWrite {
        const val EXPIRE_FOREVER = -1L
        const val EXPIRE_1_DAY = 86400L
        const val EXPIRE_60_SEC = 60L
    }
}



