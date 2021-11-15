package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.perfume.Gender

data class MemberDetailVo(
    val id: Long,
    val status: MemberStatus,
    val name: String?,
    val gender: MemberGender,
    val ageGroup: AgeGroup,
    val noteGroupIds: List<Long>
) {
    constructor(member: Member) : this(
        id = member.id,
        status = member.memberStatus,
        name = member.memberDetail.name,
        gender = member.memberDetail.gender,
        ageGroup = member.memberDetail.ageGroup,
        noteGroupIds = member.memberDetail.noteGroupIds
    )
}

fun MemberDetailVo.getPerfumeGender(): Gender {
    return when (this.gender) {
        MemberGender.FEMALE -> Gender.WOMAN
        MemberGender.MALE -> Gender.MAN
        MemberGender.UNKNOWN -> Gender.UNISEX
    }
}

fun MemberDetailVo.hasOnboard(): Boolean {
    return this.hasGender() || this.hasAgeGroup() || this.hasNoteGroupIds()
}

fun MemberDetailVo.hasGender(): Boolean {
    if (this.gender != MemberGender.UNKNOWN) return true
    return false
}

fun MemberDetailVo.hasAgeGroup(): Boolean {
    if (this.ageGroup != AgeGroup.UNKNOWN) return true
    return false
}

fun MemberDetailVo.hasNoteGroupIds(): Boolean {
    if (this.noteGroupIds.isNullOrEmpty()) return false
    return true
}