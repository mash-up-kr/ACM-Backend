package mashup.backend.spring.acm.infrastructure.spring.security

import com.fasterxml.jackson.databind.ObjectMapper
import mashup.backend.spring.acm.domain.ResultCode
import mashup.backend.spring.acm.presentation.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@EnableWebSecurity(debug = true)
@ConditionalOnWebApplication
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .mvcMatchers(
                "/error",
                "/favicon.ico",
                "/swagger-ui.html",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-resources/**",
                "/v2/api-docs"
            )
    }

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/api/v1/test/**").permitAll()
            .antMatchers("/api/v1/members/login").permitAll()
            .antMatchers("/api/**").hasAuthority(ROLE_MEMBER)
        http.csrf().disable()
        http.logout().disable()
        http.formLogin().disable()
        http.httpBasic().disable()
        http.requestCache().disable()
        http.addFilterAt(tokenPreAuthFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
//        http.sessionManagement().sessionFixation().changeSessionId()
        http.sessionManagement().disable()
        http.cors().disable()
        http.exceptionHandling()
            .authenticationEntryPoint { request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException? ->
                log.debug("authenticationEntryPoint: {}, {}, {}", request, response, authException)
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    ApiResponse.failure<Unit>(ResultCode.UNAUTHORIZED)
                )
            }
            .accessDeniedHandler { request: HttpServletRequest?, response: HttpServletResponse, accessDeniedException: AccessDeniedException? ->
                log.debug("accessDeniedHandler: {}, {}, {}", request, response, accessDeniedException)
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    ApiResponse.failure<Unit>(ResultCode.FORBIDDEN)
                )
            }
    }

    @Bean
    fun tokenPreAuthFilter(): TokenPreAuthFilter {
        val filter = TokenPreAuthFilter()
        filter.setAuthenticationManager(ProviderManager(preAuthTokenProvider()))
        return filter
    }

    @Bean
    fun preAuthTokenProvider() = PreAuthTokenProvider()

    companion object {
        val log: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
        const val ROLE_MEMBER = "MEMBER"
    }

}