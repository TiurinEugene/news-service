package test.sample.news.repository

import org.springframework.data.repository.CrudRepository
import test.sample.news.model.User

interface UsersRepository : CrudRepository<User, Long> {

    fun findByEmail(email: String): User?
}
