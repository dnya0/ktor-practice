package practice.ktor.post.services

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import practice.ktor.post.models.CreatePostRequest
import practice.ktor.post.models.Post
import practice.ktor.post.models.Posts
import practice.ktor.post.models.UpdatePostRequest
import java.time.LocalDateTime

object PostService {
    fun getAllPosts(): List<Post> = transaction {
        Posts.selectAll().map { rowToPost(it) }
    }

    fun getPostById(id: Int): Post? = transaction {
        Posts.select(Posts.id eq id)
            .map { rowToPost(it) }
            .singleOrNull()
    }

    fun createPost(request: CreatePostRequest): Post = transaction {
        val now = LocalDateTime.now()
        val id = Posts.insert {
            it[title] = request.title
            it[content] = request.content
            it[author] = request.author
            it[createdAt] = now
            it[updatedAt] = now
        } get Posts.id

        Post(
            id = id,
            title = request.title,
            content = request.content,
            author = request.author,
            createdAt = now,
            updatedAt = now
        )
    }

    fun updatePost(id: Int, request: UpdatePostRequest): Boolean = transaction {
        val existing = Posts.select(Posts.id eq id).singleOrNull() ?: return@transaction false
        
        Posts.update(where = { Posts.id eq id }) {
            request.title?.let { title -> it[Posts.title] = title }
            request.content?.let { content -> it[Posts.content] = content }
            request.author?.let { author -> it[Posts.author] = author }
            it[updatedAt] = LocalDateTime.now()
        } > 0
    }

    fun deletePost(id: Int): Boolean = transaction {
        Posts.deleteWhere(op = { Posts.id eq id }) > 0
    }

    private fun rowToPost(row: ResultRow): Post = Post(
        id = row[Posts.id],
        title = row[Posts.title],
        content = row[Posts.content],
        author = row[Posts.author],
        createdAt = row[Posts.createdAt],
        updatedAt = row[Posts.updatedAt]
    )
}
