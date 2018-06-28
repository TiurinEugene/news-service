package test.sample.news.repository

import org.springframework.data.repository.CrudRepository
import test.sample.news.model.UserRead

interface UserReadRepository : CrudRepository<UserRead, Long>
