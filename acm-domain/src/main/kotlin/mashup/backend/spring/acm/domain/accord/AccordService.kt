package mashup.backend.spring.acm.domain.accord

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface AccordService {
    fun createIfNotExists(accordCreateVo: AccordCreateVo): Accord
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
            return accord
        }
        return accordRepository.save(
            Accord(accordCreateVo = accordCreateVo)
        )
    }
}