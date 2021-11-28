package mashup.backend.spring.acm.domain.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberDetailRepository: JpaRepository<MemberDetail, Long> {
    fun findByNoteGroupIdsIsNotNull(): List<MemberDetail>
}