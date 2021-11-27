package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.domain.exception.BusinessException
import mashup.backend.spring.acm.domain.member.MemberService
import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NoteGroupCacheService(
    private val noteGroupService: NoteGroupService,
    private val memberService: MemberService,
    private val noteGroupRepository: NoteGroupRepository,
) {
    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_DEFAULT_NOTES], key = "#size")
    fun getByRandom(size: Int): List<NoteGroup> = noteGroupRepository.findByRandom(Pageable.ofSize(size))

    @Cacheable(CacheType.CacheNames.POPULAR_NOTE_GROUP)
    fun getPopularNoteGroup(): NoteGroupDetailVo {
        return findPopularNoteGroup()
    }

    @CachePut(CacheType.CacheNames.POPULAR_NOTE_GROUP)
    fun putCacheGetPopularNoteGroup(): NoteGroupDetailVo {
        return findPopularNoteGroup()
    }

    private fun findPopularNoteGroup(): NoteGroupDetailVo {
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

        return noteGroupService.getDetailById(popularOnboardNoteGroupId)
    }
}