package mashup.backend.spring.acm.infrastructure.cache

import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service

interface CacheService {
    fun clear(cacheType: CacheType)
}

@Service
class CacheServiceImpl(
    private val cacheManager: CacheManager
): CacheService {

    override fun clear(cacheType: CacheType) {
        cacheManager.getCache(cacheType.name)?.clear()
    }
}