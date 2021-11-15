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
            }.toList()
        cacheManager.setCaches(caches)

        return cacheManager
    }
}

enum class CacheType(
    val cacheName: String,
    val expiredAfterWrite: Long,
    val maximumSize: Long
) {
    // note group
    POPULAR_NOTE_GROUP("popularNoteGroup", 24 * 60 * 60, 1),

    // perfumes
    RECOMMEND_MAIN_PERFUMES("recommendMainPerfumes", 60, 10000),
    RECOMMEND_GENDER_PERFUMES("recommendGenderPerfumes", 24 * 60 * 60, 10),
    RECOMMEND_POPULAR_PERFUMES("recommendPopularPerfumes", 24 * 60 * 60, 10),
    RECOMMEND_MONTHLY_PERFUMES("recommendMonthlyPerfumes", 24 * 60 * 60, 1),
    RECOMMEND_DEFAULT_PERFUMES("recommendDefaultPerfumes", 24 * 60 * 60, 1),
    RECOMMEND_PRESENT_PERFUMES("recommendPresentPerfumes", 24 * 60 * 60, 1),


    // brand
    POPULAR_BRANDS("popularBrands", 24 * 60 * 60, 10),
}