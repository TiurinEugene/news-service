package test.sample.news

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import test.sample.news.controller.auth.AuthController

@WebMvcTest
@RunWith(MockitoJUnitRunner::class)
class NewsApplicationTests {

    @Mock
    private lateinit var authController: AuthController

    @Test
    fun contextLoads() {
        assertThat(authController).isNotNull
    }
}
