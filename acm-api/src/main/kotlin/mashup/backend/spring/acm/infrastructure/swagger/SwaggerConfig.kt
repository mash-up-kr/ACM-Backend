package mashup.backend.spring.acm.infrastructure.swagger

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
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
            .directModelSubstitute(Pageable::class.java, SwaggerPageableRequest::class.java)
            .securitySchemes(listOf(apiKey()))
            .securityContexts(securityContext())
    }

    private fun securityContext() = listOf(
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("(?!/api/v1/members/login|/api/v1/test).*"))
            .build()
    )

    private fun defaultAuth() = listOf(
        SecurityReference(
            "Bearer {accessToken}",
            arrayOf(AuthorizationScope("global", "access All"))
        )
    )

    private fun apiKey(): ApiKey {
        return ApiKey("Bearer {accessToken}", "Authorization", "header")
    }
}

//Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhY20tYXBpLWxvY2FsIiwibWVtYmVySWQiOjIyMzE1fQ.4wlepBLt-MQkFf96nDZp-LJX47pAQmeB7Gb2aSaL-4Q
// Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhY20tYXBpLWxvY2FsIiwibWVtYmVySWQiOjIyMzE1fQ.4wlepBLt-MQkFf96nDZp-LJX47pAQmeB7Gb2aSaL-4Q