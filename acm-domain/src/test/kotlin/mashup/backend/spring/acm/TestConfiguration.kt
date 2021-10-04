package mashup.backend.spring.acm

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.context.annotation.Bean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager

@SpringBootApplication
class TestConfiguration {
    @Bean
    fun transactionManager(
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