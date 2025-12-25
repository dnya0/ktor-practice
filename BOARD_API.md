# 게시판 REST API

Ktor로 구현한 게시판 REST API 서버입니다.

## 데이터베이스 설정

### 기본 설정 (H2 인메모리)

**별도 설정 없이 바로 실행 가능합니다!**

기본적으로 H2 인메모리 데이터베이스를 사용하여 자동으로 생성됩니다.
- 설치나 설정 불필요
- 서버 재시작 시 데이터 초기화 (개발/테스트용)

### PostgreSQL 사용 (선택사항)

프로덕션 환경이나 데이터 영구 저장이 필요한 경우:

```bash
export USE_POSTGRES=true
export DATABASE_URL="jdbc:postgresql://localhost:5432/ktor_board"
export DATABASE_USER="postgres"
export DATABASE_PASSWORD="postgres"
```

PostgreSQL 데이터베이스 생성:
```bash
createdb ktor_board
```

## 서버 실행

```bash
./gradlew run
```

서버는 기본적으로 `http://localhost:8080`에서 실행됩니다.

## API 엔드포인트

### 1. 모든 게시글 조회
```bash
GET /posts
```

**응답 예시:**
```json
[
  {
    "id": 1,
    "title": "첫 번째 게시글",
    "content": "게시글 내용입니다.",
    "author": "홍길동",
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  }
]
```

### 2. 특정 게시글 조회
```bash
GET /posts/{id}
```

**응답 예시:**
```json
{
  "id": 1,
  "title": "첫 번째 게시글",
  "content": "게시글 내용입니다.",
  "author": "홍길동",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

### 3. 게시글 생성
```bash
POST /posts
Content-Type: application/json

{
  "title": "새 게시글",
  "content": "게시글 내용",
  "author": "작성자"
}
```

### 4. 게시글 수정
```bash
PUT /posts/{id}
Content-Type: application/json

{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "author": "수정된 작성자"
}
```

모든 필드는 선택사항입니다. 제공된 필드만 업데이트됩니다.

### 5. 게시글 삭제
```bash
DELETE /posts/{id}
```

## 테스트 예시 (curl)

### 게시글 생성
```bash
curl -X POST http://localhost:8080/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "테스트 게시글",
    "content": "테스트 내용입니다.",
    "author": "테스터"
  }'
```

### 모든 게시글 조회
```bash
curl http://localhost:8080/posts
```

### 특정 게시글 조회
```bash
curl http://localhost:8080/posts/1
```

### 게시글 수정
```bash
curl -X PUT http://localhost:8080/posts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "수정된 제목"
  }'
```

### 게시글 삭제
```bash
curl -X DELETE http://localhost:8080/posts/1
```

## 프로젝트 구조

```
src/main/kotlin/
├── Application.kt              # 애플리케이션 진입점
├── Database.kt                 # 데이터베이스 연결 설정
├── Routing.kt                  # 라우팅 설정
├── Serialization.kt            # JSON 직렬화 설정
├── models/
│   ├── Post.kt                 # 게시글 모델 및 테이블 정의
│   └── LocalDateTimeSerializer.kt  # LocalDateTime 직렬화
├── routes/
│   └── PostRoutes.kt           # 게시판 API 라우트
└── services/
    └── PostService.kt          # 게시판 비즈니스 로직
```

## 기술 스택

- **Framework**: Ktor 3.3.1
- **Database**: PostgreSQL with Exposed ORM
- **Serialization**: kotlinx.serialization
- **Language**: Kotlin 2.2.20
