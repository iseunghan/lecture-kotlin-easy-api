package me.iseunghan.lectureeasykopringapi.blog.dto

data class BlogDto(
    val query: String,
    val sort: String,
    val page: Int = 1,
    val size: Int = 10
)