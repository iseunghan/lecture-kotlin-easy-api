package me.iseunghan.lectureeasykopringapi.blog.service

import me.iseunghan.lectureeasykopringapi.blog.dto.BlogDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BlogService {
    @Value("\${kakao.rest-api-key}")
    lateinit var kakaoRestApiKey: String

    fun searchKakao(blogDto: BlogDto): String? {
        val webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoRestApiKey")
            .build()

        val response = webClient.get()
            .uri {
                it.path("/v2/search/blog")
                    .queryParam("query", blogDto.query)
                    .queryParam("sort", blogDto.sort)
                    .queryParam("page", blogDto.page)
                    .queryParam("size", blogDto.size)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()
        return result
    }
}