package mashup.backend.spring.acm.domain.member

import mashup.backend.spring.acm.domain.member.idprovider.IdProviderType
import mashup.backend.spring.acm.domain.perfume.Gender

data class MemberDetailVo(
    val id: Long,
    val status: MemberStatus,
    val name: String?,
    val gender: Gender?,
    val age: Int?
) {
    constructor(member: Member) : this(
        id = member.id,
        status = member.memberStatus,
        name = member.memberDetail?.name,
        gender = member.memberDetail?.let { Gender.valueOf(it.gender) },
        age = member.memberDetail?.age
    )
}
