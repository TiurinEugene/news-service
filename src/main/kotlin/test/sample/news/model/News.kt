package test.sample.news.model

import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
data class News(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    val created: OffsetDateTime = now(),

    val updated: OffsetDateTime? = null,

    val text: String
)
