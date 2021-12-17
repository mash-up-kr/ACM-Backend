package mashup.backend.spring.acm.presentation.api.recommend

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.application.recommend.RecommendApplicationService
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Api(
    description = "메인 화면",
    tags = ["main"],
)
@RequestMapping("/api/v1/recommend")
@RestController
class RecommendController(
    private val recommendApplicationService: RecommendApplicationService
    ) {
    @ApiOperation(
        value = "[v1] 메인페이지 추천 API",
        notes = "메인페이지 추천 내용\n"
                + " 온보딩 완료한 경우\n"
                + " 1. For you (온보딩 맞춤 추천 향수 3개)\n"
                + " 2. 인기 브랜드\n"
                + " 3-1 {성별} 인기 함수\n"
                + " 3-2 모든 분들에게 인기가 많아요\n"
                + " 3-3 {상큼한 향}, 이 향수 어때요\n"
                + " 4. 취향을 맞춘 노트\n"
                + "\n"
                + " 온보딩 완료하지 않은 경우\n"
                + " 1. For you (전체 인기 향수 중 3개)\n"
                + " 2. 인기 브랜드\n"
                + " 3-1 이달의 추천 향수\n"
                + " 3-2 모든 분들에게 인기가 많아요\n"
                + " 3-3 선물하기 좋은 향수\n"
                + " 4. 내가 좋아할 노트\n"
    )
    @GetMapping("/main")
    fun getMainRecommend(@ApiIgnore @ModelAttribute("memberId") memberId: Long) : ApiResponse<MainRecommendData> {
        val mainPopular = recommendApplicationService.recommendMainPerfumes(memberId)

        return ApiResponse.success(MainRecommendData(mainPopular))
    }

}