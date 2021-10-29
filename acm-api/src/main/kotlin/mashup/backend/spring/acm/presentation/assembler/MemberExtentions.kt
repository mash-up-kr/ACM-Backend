package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberInitializeRequestVo
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.presentation.api.member.MemberDetailResponse
import mashup.backend.spring.acm.presentation.api.member.MemberInfoResponse
import mashup.backend.spring.acm.presentation.api.member.MemberInitializeRequest

fun MemberDetailVo.toMemberDetailResponse(): MemberDetailResponse = MemberDetailResponse(
    id = this.id,
    status = this.status.name,
    name = this.name,
    gender = this.gender?.name,
    ageGroup = this.ageGroup?.name
)

fun MemberDetailVo.toMemberInfoResponse(): MemberInfoResponse = MemberInfoResponse(
    member = this.toMemberDetailResponse()
)

fun MemberInitializeRequest.toVo(): MemberInitializeRequestVo {
    return MemberInitializeRequestVo(
        gender = this.gender?.let { Gender.valueOf(it) } ?: Gender.UNKNOWN,
        ageGroup = this.ageGroup?.let { AgeGroup.valueOf(it) } ?: AgeGroup.UNKNOWN,
        noteGroupIds = this.noteGroupIds,
    )
}