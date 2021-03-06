package test.sample.news.repository

import org.springframework.data.repository.CrudRepository
import test.sample.news.model.News

interface NewsRepository : CrudRepository<News, Long>
