package mashup.backend.spring.acm.application

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

/**
 * Application Service 를 나타내는 어노테이션
 *
 * - Domain Services : Encapsulates business logic that doesn't naturally fit within a domain object, and are NOT typical CRUD operations – those would belong to a Repository.
 * - Application Services : Used by external consumers to talk to your system (think Web Services). If consumers need access to CRUD operations, they would be exposed here.
 * - Infrastructure Services : Used to abstract technical concerns (e.g. MSMQ, email provider, etc).
 * https://stackoverflow.com/a/2279729
 */
@kotlin.annotation.Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class ApplicationService(
    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)
