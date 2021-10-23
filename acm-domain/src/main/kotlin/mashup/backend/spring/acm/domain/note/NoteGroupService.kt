package mashup.backend.spring.acm.domain.note

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteGroupService {
    fun create(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup
}

@Service
@Transactional(readOnly = true)
class NoteGroupServiceImpl(
    private val noteGroupRepository: NoteGroupRepository
): NoteGroupService {
    @Transactional(readOnly = false)
    override fun create(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup {
        return noteGroupRepository.save(NoteGroup.from(noteGroupCreateVo))
    }
}