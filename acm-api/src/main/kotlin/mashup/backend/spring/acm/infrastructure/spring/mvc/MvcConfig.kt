package mashup.backend.spring.acm.infrastructure.spring.mvc

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@ConditionalOnWebApplication
@Configuration
class MvcConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        // convert String? to Enum (https://stackoverflow.com/a/40299402)
        registry.addConverterFactory(object : ConverterFactory<String?, Enum<*>?> {
            override fun <T : Enum<*>?> getConverter(targetType: Class<T>): Converter<String?, T> {
                return Converter<String?, T> { source ->
                    try {
                        java.lang.Enum.valueOf(targetType, source) as T
                    } catch (e: Exception) {
                        throw EnumConversionFailedException()
                    }
                }
            }
        })
        super.addFormatters(registry)
    }
}