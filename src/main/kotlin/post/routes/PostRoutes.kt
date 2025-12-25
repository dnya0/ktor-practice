package practice.ktor.post.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import practice.ktor.post.models.CreatePostRequest
import practice.ktor.post.models.UpdatePostRequest
import practice.ktor.post.services.PostService

fun Route.postRoutes() {
    route("/posts") {
        // 모든 게시글 조회
        get {
            val posts = PostService.getAllPosts()
            call.respond(HttpStatusCode.OK, posts)
        }

        // 특정 게시글 조회
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
                return@get
            }

            val post = PostService.getPostById(id)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))
            } else {
                call.respond(HttpStatusCode.OK, post)
            }
        }

        // 게시글 생성
        post {
            try {
                val request = call.receive<CreatePostRequest>()
                val post = PostService.createPost(request)
                call.respond(HttpStatusCode.Created, post)
            } catch (e: Exception) {
                call.application.log.error("Error creating post", e)
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Unknown error")))
            }
        }

        // 게시글 수정
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
                return@put
            }

            val request = call.receive<UpdatePostRequest>()
            val updated = PostService.updatePost(id, request)

            if (updated) {
                val post = PostService.getPostById(id)
                call.respond(HttpStatusCode.OK, post!!)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))
            }
        }

        // 게시글 삭제
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
                return@delete
            }

            val deleted = PostService.deletePost(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Post not found"))
            }
        }
    }
}
