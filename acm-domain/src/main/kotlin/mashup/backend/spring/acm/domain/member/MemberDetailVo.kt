package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.note.NoteGroupSimpleVo
import mashup.backend.spring.acm.domain.perfume.Gender

data class MemberDetailVo(
    val id: Long,
    val status: MemberStatus,
    val name: String?,
    val gender: MemberGender,
    val ageGroup: AgeGroup,
    val noteGroupIds: List<Long>,
    val noteGroupSimpleVoList: List<NoteGroupSimpleVo>,
) {
    constructor(member: Member, noteGroupSimpleVoList: List<NoteGroupSimpleVo>) : this(
        id = member.id,
        status = member.memberStatus,
        name = member.memberDetail.name,
        gender = member.memberDetail.gender,
        ageGroup = member.memberDetail.ageGroup,
        noteGroupIds = member.memberDetail.noteGroupIds,
        noteGroupSimpleVoList = noteGroupSimpleVoList.toList(),
    )

    fun getPerfumeGender(): Gender {
        return when (this.gender) {
            MemberGender.FEMALE -> Gender.WOMAN
            MemberGender.MALE -> Gender.MAN
            MemberGender.UNKNOWN -> Gender.UNISEX
        }
    }

    fun hasOnboard(): Boolean {
        return hasGender() || hasAgeGroup() || hasNoteGroupIds()
    }

    fun hasGender(): Boolean {
        if (gender != MemberGender.UNKNOWN) return true
        return false
    }

    fun hasAgeGroup(): Boolean {
        if (ageGroup != AgeGroup.UNKNOWN) return true
        return false
    }

    fun hasNoteGroupIds(): Boolean {
        if (noteGroupIds.isNullOrEmpty()) return false
        return true
    }
}