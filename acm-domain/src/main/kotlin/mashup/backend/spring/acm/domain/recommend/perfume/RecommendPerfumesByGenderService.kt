package mashup.backend.spring.acm.domain.recommend.perfume

import mashup.backend.spring.acm.domain.member.MemberGender
import mashup.backend.spring.acm.domain.perfume.Perfume
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendPerfumesByGenderService(
    private val perfumeService: PerfumeService
): RecommendPerfumesService {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        val member = recommendRequestVo.memberDetailVo ?: return false

        return member.gender != MemberGender.UNKNOWN
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Perfume> {
        val gender = recommendRequestVo.memberDetailVo!!.getPerfumeGender()
        return perfumeService.getPerfumesByGenderWithRandom(gender, recommendRequestVo.size)
    }
}