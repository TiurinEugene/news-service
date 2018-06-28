package test.sample.news.controller.auth

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import test.sample.news.model.User
import test.sample.news.repository.UsersRepository
import javax.validation.Valid

@RestController
class AuthController(
    private val encoder: PasswordEncoder,
    private val usersRepository: UsersRepository,
    private val daoAuthenticationProvider: DaoAuthenticationProvider
) {

    @PostMapping("rest/register")
    fun register(@Valid params: AuthorizationParams): ResponseEntity<Any> {
        val user = User(
            email = params.email.toLowerCase(),
            password = encoder.encode(params.password)
        )
        usersRepository.save(user)

        return ResponseEntity(user.userId, CREATED)
    }

    @PostMapping("rest/login")
    fun login(@Valid params: AuthorizationParams): ResponseEntity<Any> {
        val (email, password) = params
        val token = UsernamePasswordAuthenticationToken(email.toLowerCase(), password)
        val auth = daoAuthenticationProvider.authenticate(token)
        SecurityContextHolder.getContext().authentication = auth

        return ResponseEntity(OK)
    }
}
