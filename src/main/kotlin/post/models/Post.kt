package practice.ktor.post.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Posts : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val content = text("content")
    val author = varchar("author", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime
)

@Serializable
data class CreatePostRequest(
    val title: String,
    val content: String,
    val author: String
)

@Serializable
data class UpdatePostRequest(
    val title: String?,
    val content: String?,
    val author: String?
)
