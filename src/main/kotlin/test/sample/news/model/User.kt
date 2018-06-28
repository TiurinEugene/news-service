package test.sample.news.model

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val userId: Long = 0,

    @Column(unique = true)
    val email: String,

    val password: String
)
