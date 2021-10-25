package mashup.backend.spring.acm.presentation.api.recommend

import io.swagger.annotations.ApiOperation
import mashup.backend.spring.acm.presentation.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/recommend")
@RestController
class RecommendController {
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
    fun getMainRecommend() : ApiResponse<MainPopularResponse> {
        // 1. 온보딩 추천 향수(온보딩) or 전체 인기 함수에서 랜덤 3개
        val mockMyRecommendPerfumes = getMockRecommendPerfumes()
        // 2. 인기 브랜드
        val mockPopularBrands = List(10) { PopularBrand("test-brand", "http://assets.stickpng.com/images/5a1ac5e0f65d84088faf1344.png")}
        // 3. gender 인기 향수(온보딩) or 이달의 추천 향수
        val mockPopularGenderOrRecommendMonthPerfumes = getMockRecommendPerfumes()
        // 4. 전체 인기 향수
        val mockPopularPerfumes = getMockRecommendPerfumes()
        // 5. 노트 그룹 기반 추천 향수(온보딩) or 선물하기 좋은 향수
        val mockRecommendGiftPerfumesOrRecommendNoteGroupPerfumes = getMockRecommendPerfumes()

        // 섹션 순서대로(3,4,5) 정렬
        val recommendPerfumesList = listOf(mockPopularGenderOrRecommendMonthPerfumes, mockPopularPerfumes, mockRecommendGiftPerfumesOrRecommendNoteGroupPerfumes)

        // 6. 노트 그룹 안의 노트 추천
        val mockRecommendNoteGroups = getMockRecommendNoteGroups()

        val mainPopular = MainPopular(
            myRecommendPerfumes = mockMyRecommendPerfumes,
            popularBrands = mockPopularBrands,
            recommendPerfumesList = recommendPerfumesList,
            recommendNoteGroups = mockRecommendNoteGroups
        )

        return ApiResponse.success(MainPopularResponse(mainPopular))
    }

    // FIXME : api개발 완료되면 삭제
    private fun getMockRecommendNoteGroups(): List<RecommendNoteGroup> {
        val recommendNoteGroups = mutableListOf<RecommendNoteGroup>()
        for (i in 0..3) {
            val recommendNotes = mutableListOf<RecommendNote>()
            for (j in 0..10) {
                val recommendNote = RecommendNote(
                    name = "note-${i}",
                    images =  List(3) { "http://assets.stickpng.com/images/5a1ac5e0f65d84088faf1344.png"},
                    recommendPerfumes = getMockRecommendPerfumes()
                )

                recommendNotes.add(recommendNote)
            }
            recommendNoteGroups.add(RecommendNoteGroup("noteGroup-${i}", recommendNotes))
        }

        return recommendNoteGroups
    }

    // FIXME : api개발 완료되면 삭제
    private fun getMockRecommendPerfumes(): List<SimpleRecommendPerfume> {
        val mockPopularPerfumes = mutableListOf<SimpleRecommendPerfume>()
        for (i in 0..10) {
            mockPopularPerfumes.add(SimpleRecommendPerfume(1L,"https://fimgs.net/mdimg/perfume/375x500.1834.jpg", "brand-${i}", "name-${i}"))
        }

        return mockPopularPerfumes
    }
}