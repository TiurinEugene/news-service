package test.sample.news.controller.news

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import test.sample.news.model.News
import test.sample.news.model.UserRead
import test.sample.news.repository.NewsRepository
import test.sample.news.repository.UserReadRepository
import test.sample.news.repository.UsersRepository
import test.sample.news.security.CustomUserPrincipal
import java.time.OffsetDateTime.now

@RestController
@RequestMapping("rest/news")
class NewsController(
    private val newsRepository: NewsRepository,
    private val usersRepository: UsersRepository,
    private val userReadRepository: UserReadRepository,
    private val brokerMessagingTemplate: SimpMessagingTemplate
) {

    @PostMapping
    fun createNews(text: String): ResponseEntity<Long> {
        val news = News(text = text)
        newsRepository.save(news)
        brokerMessagingTemplate.convertAndSend("/news", news)
        return ResponseEntity(news.id, CREATED)
    }

    @GetMapping
    fun getNews(): ResponseEntity<Any> {
        val news = newsRepository.findAll().sortedBy { it.created }
        return if (news.isEmpty())
            ResponseEntity(NOT_FOUND)
        else
            ResponseEntity(news, OK)
    }

    @GetMapping("/{id}")
    fun getSingleNews(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val news = newsRepository.findById(id)
        return if (!news.isPresent)
            ResponseEntity(NOT_FOUND)
        else
            ResponseEntity(news, OK)
    }

    @PatchMapping("/{id}")
    fun updateNews(@PathVariable("id") id: Long, text: String): ResponseEntity<Any> {
        val news = newsRepository.findById(id)
        return if (!news.isPresent)
            ResponseEntity(NOT_FOUND)
        else {
            val newNews = news.get().copy(text = text, updated = now())
            brokerMessagingTemplate.convertAndSend("/news", newNews)
            ResponseEntity(newsRepository.save(newNews).id, OK)
        }
    }

    @PutMapping("/{id}")
    fun replaceNews(@PathVariable("id") id: Long, text: String): ResponseEntity<Any> {
        val news = newsRepository.findById(id)
        return if (!news.isPresent)
            ResponseEntity(newsRepository.save(News(text = text)).id, CREATED)
        else {
            val newNews = News(text = text)
            newsRepository.delete(news.get())
            newsRepository.save(newNews)
            brokerMessagingTemplate.convertAndSend("/news", newNews)
            ResponseEntity(newNews.id, OK)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNews(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val news = newsRepository.findById(id)
        return if (!news.isPresent)
            ResponseEntity(NOT_FOUND)
        else {
            newsRepository.delete(news.get())
            ResponseEntity(OK)
        }
    }

    @PostMapping("/read/{id}")
    fun readNews(@PathVariable("id") id: Long, @AuthenticationPrincipal currentUser: CustomUserPrincipal): ResponseEntity<Any> {
        val user = usersRepository.findByEmail(currentUser.email)
        val news = newsRepository.findById(id)
        if (user == null || !news.isPresent)
            return ResponseEntity(NOT_FOUND)

        val objectMapper = jacksonObjectMapper()
        return if (userReadRepository.findById(user.userId).isPresent) {
            val userRead = userReadRepository.findById(user.userId)
            val readNewsList = objectMapper.readValue<MutableSet<Long>>(userRead.get().newsId)
            userReadRepository.save(userRead.get().copy(newsId = objectMapper.writeValueAsString(readNewsList + id)))
            ResponseEntity(OK)
        } else {
            val readNewsList = objectMapper.writeValueAsString(listOf(id))
            ResponseEntity(userReadRepository.save(UserRead(user.userId, readNewsList)), CREATED)
        }
    }
}
