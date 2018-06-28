package test.sample.news.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import test.sample.news.repository.UsersRepository

@Service
class CustomUserDetailsService(private val usersRepository: UsersRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByEmail(username) ?: throw UsernameNotFoundException(username)
        return CustomUserPrincipal(user.email, user.password)
    }
}
