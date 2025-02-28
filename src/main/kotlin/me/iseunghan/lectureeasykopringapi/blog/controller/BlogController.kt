package me.iseunghan.lectureeasykopringapi.blog.controller

import jakarta.validation.Valid
import me.iseunghan.lectureeasykopringapi.blog.dto.BlogDto
import me.iseunghan.lectureeasykopringapi.blog.service.BlogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BlogController(
    val blogService: BlogService
) {

    @GetMapping("/api/blog")
    fun search(
        @RequestBody @Valid blogDto: BlogDto
    ): String? {
        return blogService.searchKakao(blogDto)
    }

    @GetMapping("/api/word/rank")
    fun searchTop100Word(): Map<String, Int>? {
        return blogService.searchWordRank()
    }
}