package mashup.backend.spring.acm.presentation.api.recommend

import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.brand.BrandApplicationService
import mashup.backend.spring.acm.application.recommend.RecommendApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RequestMapping("/api/v1/recommend")
@RestController
class RecommendController(
    private val recommendApplicationService: RecommendApplicationService
    ) {
    @ApiOperation(
        value = "[v1] 메인페이지 추천 API",
        notes = "메인페이지 추천 내용\n"
                + " 1. 온보딩 맞춤 추천 향수 or 전체 인기 향수 랜덤 데이터(온보딩x)\n"
                + " 2. 인기 브랜드\n"
                + " 3. gender 인기 함수 or 이달의 추천 향수(온보딩x)\n"
                + " 4. 전체 인기 향수\n"
                + " 5. 노트 그룹 기반 추천 향수 or 선물하기 좋은 향수(온보딩x)\n"
                + "\n"
                + "- recommendPerfumesList는 위의 3,4,5를 순서대로 정렬하여 내려줍니다."
    )
    @GetMapping("/main")
    fun getMainRecommend(@ApiIgnore @ModelAttribute("memberId") memberId: Long) : ApiResponse<MainRecommendResponse> {
        val mainPopular = recommendApplicationService.recommendMainPerfumes(memberId)

        return ApiResponse.success(MainRecommendResponse(mainPopular))
    }

}