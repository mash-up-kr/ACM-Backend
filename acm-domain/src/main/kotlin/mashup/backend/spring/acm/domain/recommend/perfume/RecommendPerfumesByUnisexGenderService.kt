package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByUnisexGenderService(
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val member = recommendRequestVo.memberDetailVo ?: return true

        return !member.hasGender()
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        return perfumeService.getPerfumesByGenderWithRandom(Gender.UNISEX, recommendRequestVo.size)
    }
}