package mashup.backend.spring.acm.presentation.assembler

import mashup.backend.spring.acm.domain.member.AgeGroup
import mashup.backend.spring.acm.domain.member.MemberDetailVo
import mashup.backend.spring.acm.domain.member.MemberGender
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

fun MemberDetailVo.getPerfumeGender(): Gender {
    return when (this.gender) {
        MemberGender.FEMALE -> Gender.WOMAN
        MemberGender.MALE -> Gender.MAN
        else -> Gender.UNISEX
    }
}

fun MemberDetailVo.toMemberInfoResponse(): MemberInfoResponse = MemberInfoResponse(
    member = this.toMemberDetailResponse()
)

fun MemberDetailVo.hasOnboard(): Boolean {
    return this.hasGender() || this.hasAgeGroup() || this.hasNoteGroupIds()
}

fun MemberDetailVo.hasGender(): Boolean {
    if (this.gender != null && this.gender != MemberGender.UNKNOWN) return true
    return false
}

fun MemberDetailVo.hasAgeGroup(): Boolean {
    if (this.ageGroup != null && this.ageGroup != AgeGroup.UNKNOWN) return true
    return false
}

fun MemberDetailVo.hasNoteGroupIds(): Boolean {
    if (this.noteGroupIds.isNullOrEmpty()) return false
    return true
}

fun MemberInitializeRequest.toVo(): MemberInitializeRequestVo {
    return MemberInitializeRequestVo(
        gender = this.gender?.let { MemberGender.valueOf(it) } ?: MemberGender.UNKNOWN,
        ageGroup = this.ageGroup?.let { AgeGroup.valueOf(it) } ?: AgeGroup.UNKNOWN,
        noteGroupIds = this.noteGroupIds,
    )
}