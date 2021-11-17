package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.exception.NoteGroupNotFoundException
import mashup.backend.spring.acm.domain.exception.PerfumeNotFoundException
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.domain.perfume.PerfumeRepository
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NoteGroupService {
    fun create(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup
    fun getNoteGroupByName(originalName: String): NoteGroup?
    fun findAll(): List<NoteGroup>
    fun getByRandom(size: Int): List<NoteGroup>
    fun getRecommendNoteGroupId(perfumeIds: Set<Long>, noteGroupIds: List<Long>): NoteGroupDetailVo
    fun getDetailById(noteGroupId: Long): NoteGroupDetailVo
    fun getById(noteGroupId: Long): NoteGroup?
    fun getPopularNoteGroup(): NoteGroupDetailVo
}

@Service
@Transactional(readOnly = true)
class NoteGroupServiceImpl(
    private val memberService: MemberService,
    private val noteGroupRepository: NoteGroupRepository,
    private val perfumeRepository: PerfumeRepository
) : NoteGroupService {
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


    override fun getByRandom(size: Int): List<NoteGroup> = noteGroupRepository.findByRandom(Pageable.ofSize(size))

    /**
     * 해당 메소드를 사용할 때 noteGroupIds가 null이나 empty가 아닌지 검증하고 사용해야한다.
     */
    override fun getRecommendNoteGroupId(perfumeIds: Set<Long>, noteGroupIds: List<Long>): NoteGroupDetailVo {
        if (noteGroupIds.isNullOrEmpty())
            throw IllegalArgumentException("서버에서 잘못된 요청을 허용했습니다. noteGroupIds: $noteGroupIds")
        if (perfumeIds.isNullOrEmpty()) return getDetailById(noteGroupIds.shuffled()[0])

        var perfume = perfumeRepository.findPerfumeById(perfumeIds.elementAt(0))
            ?: throw PerfumeNotFoundException("Perfume not found. id: ${perfumeIds.elementAt(0)}")
        var perfumeNoteGroupIds = perfume.notes
            .mapNotNull { it.note.noteGroup?.id }
            .toSet()
        if (perfumeIds.size > 1)  {
            var unionNoteGroupIds = perfumeNoteGroupIds
            for (index in 1 until perfumeIds.size) {
                unionNoteGroupIds = unionNoteGroupIds.filter {
                    perfume = perfumeRepository.findPerfumeById(perfumeIds.elementAt(index))
                        ?: throw PerfumeNotFoundException("Perfume not found. id: ${perfumeIds.elementAt(index)}")
                    perfume.notes
                        .mapNotNull { it.note.noteGroup?.id }
                        .toSet()
                        .contains(index.toLong())
                }
                    .toSet()
            }
            if (unionNoteGroupIds.isNotEmpty()) perfumeNoteGroupIds = unionNoteGroupIds
        }

        val unionNoteGroupIds = perfumeNoteGroupIds.filter {
            noteGroupIds.contains(it)
        }.toSet()
        if (unionNoteGroupIds.isNotEmpty()) {
            return getDetailById(unionNoteGroupIds.elementAt(0))
        }

        return getDetailById(perfumeNoteGroupIds.elementAt(0))
    }

    override fun getDetailById(noteGroupId: Long): NoteGroupDetailVo = noteGroupRepository.findByIdOrNull(noteGroupId)
        ?.let { NoteGroupDetailVo(it) }
        ?: throw NoteGroupNotFoundException(noteGroupId = noteGroupId)

    override fun getById(noteGroupId: Long): NoteGroup? {
        return noteGroupRepository.findNoteGroupById(noteGroupId)
    }

    @Cacheable(CacheType.CacheNames.POPULAR_NOTE_GROUP)
    override fun getPopularNoteGroup(): NoteGroupDetailVo {
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
}