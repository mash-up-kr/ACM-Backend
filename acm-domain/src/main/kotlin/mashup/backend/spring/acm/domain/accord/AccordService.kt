package mashup.backend.spring.acm.domain.accord

import mashup.backend.spring.acm.domain.exception.AccordNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface AccordService {
    fun createIfNotExists(accordCreateVo: AccordCreateVo): Accord
    fun getById(accordId: Long): Accord
    fun getAccords(pageable: Pageable): Page<Accord>
}

@Service
@Transactional(readOnly = true)
class AccordServiceImpl(
    private val accordRepository: AccordRepository,
) : AccordService {

    @Transactional
    override fun createIfNotExists(accordCreateVo: AccordCreateVo): Accord {
        val accord = accordRepository.findByName(name = accordCreateVo.name)
        if (accord != null) {
            if (accord.textColor != accordCreateVo.textColor) {
                log.error("textColor 값이 일치하지 않습니다. accord: ${accord.textColor}, accordCreateVo: ${accordCreateVo.textColor}")
            }
            if (accord.backgroundColor != accordCreateVo.backgroundColor) {
                log.error("backgroundColor 값이 일치하지 않습니다. accord: ${accord.backgroundColor}, accordCreateVo: ${accordCreateVo.backgroundColor}")
            }
            return accord
        }
        return accordRepository.save(
            Accord(accordCreateVo = accordCreateVo)
        )
    }

    override fun getById(accordId: Long): Accord = accordRepository.findByIdOrNull(accordId)
        ?: throw AccordNotFoundException(accordId = accordId)

    override fun getAccords(pageable: Pageable): Page<Accord> = accordRepository.findAll(pageable)

    companion object {
        val log: Logger = LoggerFactory.getLogger(AccordServiceImpl::class.java)
    }
}