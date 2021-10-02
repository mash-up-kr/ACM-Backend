package mashup.backend.spring.acm.infrastructure.swagger

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Profile("swagger")
@ConditionalOnWebApplication
@EnableSwagger2
@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .paths(PathSelectors.ant("/**"))
            .build()
            .globalOperationParameters(globalParameterList())
            .directModelSubstitute(Pageable::class.java, SwaggerPageableRequest::class.java)
    }

    private fun globalParameterList(): List<Parameter> {
        return listOf(
            ParameterBuilder()
                .name("Authorization")
                .modelRef(ModelRef("string"))
                .required(false)
                .parameterType("header")
                .description("bearer {accessToken}")
                .build()
        )
    }
}