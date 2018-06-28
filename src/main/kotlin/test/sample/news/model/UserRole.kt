package test.sample.news.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class UserRole(

    @Id
    val userId: Long,

    val userRole: String
)
