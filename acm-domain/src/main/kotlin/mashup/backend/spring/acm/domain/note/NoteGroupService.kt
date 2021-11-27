package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.exception.NoteGroupNotFoundException
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.perfume.PerfumeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteGroupService {
    fun create(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup
    fun getNoteGroupByName(originalName: String): NoteGroup?
    fun findAll(): List<NoteGroup>
    fun getDetailById(noteGroupId: Long): NoteGroupDetailVo
    fun getById(noteGroupId: Long): NoteGroup?
    fun getByRandom(size: Int): List<NoteGroup>
    fun findPopularNoteGroup(): NoteGroupDetailVo
    fun getNoteGroups(pageable: Pageable): Page<NoteGroup>
    fun getNoteGroupsByIdIn(noteGroupIds: List<Long>): List<NoteGroupSimpleVo>
}

@Service
@Transactional(readOnly = true)
class NoteGroupServiceImpl(
    private val noteGroupRepository: NoteGroupRepository,
    private val perfumeRepository: PerfumeRepository,
) : NoteGroupService {
    @Autowired
    lateinit var memberService: MemberService

    @Transactional(readOnly = false)
    override fun create(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup {
        if (noteGroupRepository.existsByOriginalName(noteGroupCreateVo.name)) {
            throw RuntimeException("노트그룹이 이미 존재합니다. name: ${noteGroupCreateVo.name}") // TODO: BusinessException
        }
        return noteGroupRepository.save(NoteGroup.from(noteGroupCreateVo))
    }

    override fun getNoteGroupByName(originalName: String): NoteGroup? =
        noteGroupRepository.findByOriginalName(originalName)

    override fun findAll(): List<NoteGroup> = noteGroupRepository.findAll()

    override fun getDetailById(noteGroupId: Long): NoteGroupDetailVo = noteGroupRepository.findByIdOrNull(noteGroupId)
        ?.let { NoteGroupDetailVo(it) }
        ?: throw NoteGroupNotFoundException(noteGroupId = noteGroupId)

    override fun getById(noteGroupId: Long): NoteGroup? {
        return noteGroupRepository.findNoteGroupById(noteGroupId)
    }

    override fun getByRandom(size: Int): List<NoteGroup> = noteGroupRepository.findByRandom(Pageable.ofSize(size))

    override fun findPopularNoteGroup(): NoteGroupDetailVo {
        var maxCount = 0L
        var popularOnboardNoteGroupId = -1L
        val countMap = mutableMapOf<Long, Long>()

        val noteGroupIdsList = memberService.findAllMemberDetail().map { it.noteGroupIds }
        for (noteGroupIds in noteGroupIdsList) {
            noteGroupIds.forEach {
                if (countMap.containsKey(it)) {
                    countMap[it] = countMap[it]!!.plus(1)
                } else {
                    countMap[it] = 1
                }

                if (maxCount < countMap[it]!!) {
                    maxCount = countMap[it]!!
                    popularOnboardNoteGroupId = it
                }
            }
        }

        if (popularOnboardNoteGroupId == -1L) {
            throw BusinessException(ResultCode.ONBOARD_DATA_NOT_EXIST, ResultCode.ONBOARD_DATA_NOT_EXIST.message)
        }

        return getDetailById(popularOnboardNoteGroupId)
    }

    override fun getNoteGroups(pageable: Pageable): Page<NoteGroup> = noteGroupRepository.findAll(pageable)

    override fun getNoteGroupsByIdIn(noteGroupIds: List<Long>): List<NoteGroupSimpleVo> =
        noteGroupRepository.findByIdIn(noteGroupIds = noteGroupIds)
            .map { NoteGroupSimpleVo(it) }
}