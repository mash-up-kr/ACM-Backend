package mashup.backend.spring.acm.presentation.api.perfume

import mashup.backend.spring.acm.domain.perfume.Gender

data class PerfumeDetailResponse(
    val perfumeDetail: PerfumeDetail
)

data class PerfumeDetail(
    val id: Long,
    val name: String,
    val brandName: String,
    val gender: Gender,
    val description: String,
    val imageUrl: String,
    val thumbnailImageUrl: String,
    val accords: List<SimplePerfumeAccord>,
    val notes: SimplePerfumeNotes,
    val similarPerfumes: List<PerfumeSimpleResponse>,
)

data class SimplePerfumeNotes(
    val top: List<String>,
    val middle: List<String>,
    val base: List<String>,
    val unknown: List<String>
)

data class SimplePerfumeAccord(
    val name: String,
    val score: Double?,
    val opacity: Double?,
    val backgroundColor: String,
    val textColor: String,
)

data class PerfumeSimpleResponse(
    val id: Long,
    val name: String,
    val thumbnailImageUrl: String,
    val brandName: String,
)

data class PerfumeListData(
    val perfumes: List<PerfumeSimpleResponse>
)