package practice.ktor

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import practice.ktor.post.models.Posts

object DatabaseFactory {
    fun init() {
        // H2 인메모리 데이터베이스를 기본으로 사용 (자동 생성)
        // PostgreSQL을 사용하려면 USE_POSTGRES=true 환경 변수 설정
        val usePostgres = System.getenv("USE_POSTGRES")?.toBoolean() ?: false
        
        val database = if (usePostgres) {
            val jdbcURL = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/ktor_board"
            val user = System.getenv("DATABASE_USER") ?: "postgres"
            val password = System.getenv("DATABASE_PASSWORD") ?: "postgres"
            Database.connect(jdbcURL, "org.postgresql.Driver", user, password)
        } else {
            // H2 인메모리 데이터베이스 (자동 생성, 별도 설정 불필요)
            Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                driver = "org.h2.Driver"
            )
        }
        
        transaction(database) {
            SchemaUtils.create(Posts)
        }
    }
}
