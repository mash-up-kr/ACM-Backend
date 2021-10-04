package mashup.backend.spring.acm

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.batch.BatchDataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@EnableBatchProcessing
@Configuration
class BatchConfig(
    @Value("\${spring.batch.datasource.url}")
    private val url: String,
    @Value("\${spring.batch.datasource.username}")
    private val username: String,
    @Value("\${spring.batch.datasource.password}")
    private val password: String
) {
    companion object {
        const val SPRING_BATCH_JOB_NAMES = "spring.batch.job.names"
    }

    @Bean
    @BatchDataSource
    fun batchDataSource(): DataSource = DataSourceBuilder.create()
        .url(url)
        .username(username)
        .password(password)
        .build()
}