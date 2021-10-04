package mashup.backend.spring.acm.infrastructure.swagger

import io.swagger.annotations.ApiModelProperty

data class SwaggerPageableRequest(
    @ApiModelProperty(value = "Number of records per page", example = "20")
    val size: Int? = null,

    @ApiModelProperty(value = "Results page you want to retrieve (0..N)", example = "0")
    val page: Int? = null,

    @ApiModelProperty(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
    val sort: String? = null
)