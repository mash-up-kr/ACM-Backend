package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import mashup.backend.spring.acm.domain.recommend.Recommender
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PerfumeRecommenderCacheService(
    private val perfumesByGenderRecommender: Recommender<Perfume>,
    private val popularPerfumesRecommender: Recommender<Perfume>,
) {
    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun recommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        findPerfumesByGender(memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_GENDER_PERFUMES], key = "#memberDetailVo.gender.name() + #size")
    fun putCacheRecommendPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        findPerfumesByGender(memberDetailVo, size)

    private fun findPerfumesByGender(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(perfumesByGenderRecommender, memberDetailVo, size)

    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun recommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        findPopularPerfumes(memberDetailVo, size)

    @CachePut(value = [CacheType.CacheNames.RECOMMEND_POPULAR_PERFUMES], key = "#size")
    fun putCacheRecommendPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        findPopularPerfumes(memberDetailVo, size)

    private fun findPopularPerfumes(memberDetailVo: MemberDetailVo, size: Int) =
        recommend(popularPerfumesRecommender, memberDetailVo, size)

    private fun recommend(recommender: Recommender<Perfume>, memberDetailVo: MemberDetailVo, size: Int) = recommender
        .recommend(memberDetailVo, size)
        .map { PerfumeSimpleVo(it) }
}