package test.sample.news.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun daoAuthenticationProvider(customUserDetailsService: CustomUserDetailsService): DaoAuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setPasswordEncoder(encoder())
            setUserDetailsService(customUserDetailsService)
        }


    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests().antMatchers("/rest/register").anonymous()
            .and()
            .authorizeRequests().antMatchers("/rest/login").anonymous()
            .and()
            .authorizeRequests().antMatchers("/echo").anonymous()
            .and()
            .authorizeRequests().anyRequest().authenticated()
    }
}
