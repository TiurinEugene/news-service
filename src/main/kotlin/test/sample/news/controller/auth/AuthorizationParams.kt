package test.sample.news.controller.auth

import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class AuthorizationParams(

    @get:Email
    val email: String,

    @get:Size(min = 1, max = 60)
    val password: String
)
