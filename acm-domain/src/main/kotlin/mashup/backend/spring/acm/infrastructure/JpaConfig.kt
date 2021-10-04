package mashup.backend.spring.acm.infrastructure

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager

@EnableJpaAuditing
@Configuration
class JpaConfig {
    @Bean
    @Primary
    fun acmTransactionManager(
        transactionManagerCustomizers: ObjectProvider<TransactionManagerCustomizers>
    ): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManagerCustomizers.ifAvailable { customizers: TransactionManagerCustomizers ->
            customizers.customize(
                transactionManager
            )
        }
        return transactionManager
    }
}