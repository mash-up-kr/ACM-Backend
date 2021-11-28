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
        val caches = CacheType.values().map { convertCaffeineCache(it) }
        cacheManager.setCaches(caches)

        return cacheManager
    }

    private fun convertCaffeineCache(cacheType: CacheType): CaffeineCache {
        if (cacheType.hasCacheExpiredAfterWrite()) {
            return CaffeineCache(
                cacheType.cacheName,
                Caffeine.newBuilder()
                    .expireAfterWrite(cacheType.expiredAfterWrite!!, TimeUnit.SECONDS)
                    .maximumSize(cacheType.maximumSize)
                    .build()
            )

        }

        return CaffeineCache(
            cacheType.cacheName,
            Caffeine.newBuilder()
                .maximumSize(cacheType.maximumSize)
                .build()
        )
    }
}

enum class CacheType(
    val cacheName: String,
    val expiredAfterWrite: Long?, // second
    val maximumSize: Long
) {
    // note group
    POPULAR_NOTE_GROUP(CacheNames.POPULAR_NOTE_GROUP, CacheExpiredAfterWrite.EXPIRE_1_DAY, 1),

    // perfumes
    PERFUMES_BY_NOTE_ID(CacheNames.PERFUMES_BY_NOTE_ID, null, 10000),
    RECOMMEND_GENDER_PERFUMES(CacheNames.RECOMMEND_GENDER_PERFUMES, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10),
    RECOMMEND_POPULAR_PERFUMES(CacheNames.RECOMMEND_POPULAR_PERFUMES, CacheExpiredAfterWrite.EXPIRE_1_DAY, 10),
    RECOMMEND_MONTHLY_PERFUMES(CacheNames.RECOMMEND_MONTHLY_PERFUMES, null, 10),
    RECOMMEND_DEFAULT_PERFUMES(CacheNames.RECOMMEND_DEFAULT_PERFUMES, null, 10),
    RECOMMEND_PRESENT_PERFUMES(CacheNames.RECOMMEND_PRESENT_PERFUMES, null, 10),

    // recommend notes
    RECOMMEND_DEFAULT_NOTES(CacheNames.RECOMMEND_DEFAULT_NOTES, null, 10),

    // brand
    POPULAR_BRANDS(CacheNames.POPULAR_BRANDS, null, 10);

    object CacheNames {
        // note group
        const val POPULAR_NOTE_GROUP = "popularNoteGroup"

        // perfumes
        const val PERFUMES_BY_NOTE_ID = "perfumesByNoteId"

        // recommend perfumes
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
        const val EXPIRE_1_DAY = 86400L
        const val EXPIRE_60_SEC = 60L
    }

    fun hasCacheExpiredAfterWrite(): Boolean {
        return this.expiredAfterWrite != null
    }
}



