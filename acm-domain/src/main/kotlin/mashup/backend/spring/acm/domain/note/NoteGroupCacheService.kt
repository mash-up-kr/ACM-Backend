package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.infrastructure.CacheType
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class NoteGroupCacheService(
    private val noteGroupService: NoteGroupService,
) {
    @Cacheable(value = [CacheType.CacheNames.RECOMMEND_DEFAULT_NOTES], key = "#size")
    fun getByRandom(size: Int): List<NoteGroup> = noteGroupService.getByRandom(size)

    @Cacheable(CacheType.CacheNames.POPULAR_NOTE_GROUP)
    fun getPopularNoteGroup(): NoteGroupDetailVo {
        return noteGroupService.findPopularNoteGroup()
    }

    @CachePut(CacheType.CacheNames.POPULAR_NOTE_GROUP)
    fun putCacheGetPopularNoteGroup(): NoteGroupDetailVo {
        return noteGroupService.findPopularNoteGroup()
    }


}