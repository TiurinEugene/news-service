package test.sample.news.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class UserRead(

    @Id
    val userId: Long,

    val newsId: String
)
