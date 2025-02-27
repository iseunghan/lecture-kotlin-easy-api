package me.iseunghan.lectureeasykopringapi.blog.service

import me.iseunghan.lectureeasykopringapi.blog.dto.BlogDto
import me.iseunghan.lectureeasykopringapi.blog.entity.WordCount
import org.springframework.stereotype.Service

@Service
class BlogService {
    fun searchKakao(blogDto: BlogDto): String? {
        println("Search Kakao: $blogDto")
        return "SearchKaKao"
    }
}