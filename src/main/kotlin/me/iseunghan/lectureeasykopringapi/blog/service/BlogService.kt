package me.iseunghan.lectureeasykopringapi.blog.service

import me.iseunghan.lectureeasykopringapi.blog.dto.BlogDto
import me.iseunghan.lectureeasykopringapi.blog.entity.WordCount
import me.iseunghan.lectureeasykopringapi.blog.repository.WordRepository
import me.iseunghan.lectureeasykopringapi.core.exception.InvalidInputException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class BlogService(
    val wordRepository: WordRepository,
    @Value("\${kakao.rest-api-key}") var kakaoRestApiKey: String
) {

    @Transactional
    fun searchKakao(blogDto: BlogDto): String? {
        countWord(blogDto.query.lowercase())
        return retrieveBlogArticles(blogDto).block()
    }

    protected fun countWord(query: String) {
        val wordCount = wordRepository.findById(query).orElse(WordCount(word = query))
        wordCount.increaseCnt()
        wordRepository.save(wordCount)
    }

    private fun retrieveBlogArticles(blogDto: BlogDto): Mono<String> {
        val webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoRestApiKey")
            .build()

        return webClient.get()
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
    }

    @Transactional(readOnly = true)
    public fun searchWordRank(): Map<String, Int> {
        return wordRepository.findTop10ByOrderByCntDesc().associate { it.word to it.cnt }
    }


    private fun validBlogDto(blogDto: BlogDto) {
        val msgList = mutableListOf<ExceptionMsg>()

        if (!StringUtils.hasText(blogDto.query)) {
            msgList.add(ExceptionMsg.EMPTY_QUERY)
        }
        if (blogDto.sort !in arrayOf("ACCURACY", "RECENCY")) {
            msgList.add(ExceptionMsg.NOT_IN_SORT)
        }
        when {
            blogDto.page < 1 -> msgList.add(ExceptionMsg.LESS_THAN_MIN)
            blogDto.page > 50 -> msgList.add(ExceptionMsg.MORE_THAN_MAX)
        }

        if (msgList.isNotEmpty()) {
            val message = msgList.joinToString { it.msg }
            throw InvalidInputException(message)
        }
    }
}

private enum class ExceptionMsg(val msg: String) {
    EMPTY_QUERY("query parameter required"),
    NOT_IN_SORT("sort parameter one of accuracy and recency"),
    LESS_THAN_MIN("page is less than min"),
    MORE_THAN_MAX("page is more than max"),
}