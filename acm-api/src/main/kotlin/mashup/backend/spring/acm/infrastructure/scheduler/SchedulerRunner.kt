package mashup.backend.spring.acm.infrastructure.scheduler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SchedulerRunner(private val schedulers: List<Scheduler>): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        schedulers.forEach { scheduler ->
            log.info("[${scheduler.javaClass.name}] init start ======================")
            scheduler.init()
            log.info("====================== init end [${scheduler.javaClass.name}]")
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(SchedulerRunner::class.java)
    }
}