package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PerfumeRecommenderCacheService(
    private val perfumeRecommenderService: PerfumeRecommenderService,
) {
    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun recommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        perfumeRecommenderService.findPerfumesByGender(memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun putCacheRecommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        perfumeRecommenderService.findPerfumesByGender(memberDetailVo, size)

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun recommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        perfumeRecommenderService.findPopularPerfumes(memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun putCacheRecommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        perfumeRecommenderService.findPopularPerfumes(memberDetailVo, size)


}