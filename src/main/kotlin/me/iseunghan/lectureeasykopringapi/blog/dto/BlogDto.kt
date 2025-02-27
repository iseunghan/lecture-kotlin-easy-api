package me.iseunghan.lectureeasykopringapi.blog.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import me.iseunghan.lectureeasykopringapi.core.annotation.ValidEnum

data class BlogDto(
    @field:NotBlank(message = "query parameter required")
    val query: String,

    @field:NotBlank(message = "sort parameter required")
    @field:ValidEnum(enumClass = EnumSort::class, message = "sort parameter one of ACCURACY and RECENCY")
    val sort: String,

    @field:NotNull(message = "sort parameter required")
    @field:Min(1, message = "sort parameter min")
    @field:Max(50, message = "sort parameter max")
    val page: Int = 1,

    @field:NotNull(message = "size parameter required")
    @field:Min(1, message = "sort parameter min")
    @field:Max(50, message = "sort parameter max")
    val size: Int = 10
) {
    private enum class EnumSort {
        ACCURACY,
        RECENCY
    }
}