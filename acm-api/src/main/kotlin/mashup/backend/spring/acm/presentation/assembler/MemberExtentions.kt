package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.presentation.api.member.MemberDetailResponse
import mashup.backend.spring.acm.presentation.api.member.MemberInfoResponse

fun MemberDetailVo.toMemberDetailResponse(): MemberDetailResponse = MemberDetailResponse(
    id = this.id,
    status = this.status.name,
    name = this.name,
    gender = this.gender?.name,
    age = this.age
)

fun MemberDetailVo.toMemberInfoResponse(): MemberInfoResponse {
    return MemberInfoResponse(
        member = this.toMemberDetailResponse()
    )
}