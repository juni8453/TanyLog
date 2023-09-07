## 📝 Tanylog

---
#### 💁🏻 `프로젝트명` Tanylog API
#### 📆 `기간` 2023.07.26 ~ 2023.09.07
#### 📝 [한 눈에 보는 프로젝트 Notion 문서](https://concrete-blanket-9f9.notion.site/API-dc1a77668287447fa62575f273720631?pvs=4)
#### 📝 [Spring Rest Docs](http://15.165.146.174/docs/index.html)

<br>

### ⚒ 사용 기술 스택

---
<details>
<summary> 1. BackEnd </summary>
<div>

- Java 11
- Gradle
- MySQL 8
- Spring Boot
- Spring Data JPA
- Querydsl
- Spring Security
- Spring Rest Docs
- Junit5

</div>
</details>

<details>
<summary> 2. Devops </summary>
<div>

- AWS EC2
- GitHub Actions
- Docker

</div>
</details>
   
<details>
<summary> 3. Tools </summary>
<div>

- IntelliJ
- Git, GitHub

</div>
</details>
   
<br>

### 💡 API 설계

---

| Domain | Method | Description | URI |
|-----------|--------|-----------|-------------|
| 게시글      | POST | 게시글 등록   | POST /posts |
|           | DELETE | 게시글 삭제  | DELETE /posts/{postId} |
|           | PUT | 게시글 수정    | PUT /posts/{postId} |
|           | GET | 게시글 단건 조회 | GET /posts/{postId} |       
|           | GET | 게시글 전체 조회 | GET /posts |
|           | GET | 전체 조회 페이징 처리 | GET /posts?page=?&size=? |
|           | GET | 전체 조회 검색 처리 | GET /posts?searchType=?&keyword=? |
|           | GET | 전체 조회 페이징 + 검색 | GET /posts/?page=?&size=?&searchType=?&keyword=? |
|           | GET | 내가 작성한 게시글 조회 | GET /posts/my_posts |
|           | POST | 게시글 좋아요 | POST /posts/{postId}/like |
| 댓글       | POST | 댓글 등록 | POST /posts/{postId}/comments |
|           | POST | 대댓글 등록 | POST /posts/{postId}/comments/{commentId}/reply |
|           | DELETE | 댓글 삭제 | DELETE /comments/{commentId} |
|           | PUT | 댓글 수정 | PUT /comments{commentId} |
|           | GET | 댓글 전체 조회 | GET /posts/{postId}/comments |
|           | GET | 대댓글 전체 조회 | GET /posts/{postId}/comments/{commentId} |

<br>

### 💡 DB ERD

---
<img width="583" alt="tanylog_erd" src="https://github.com/juni8453/Tanylog/assets/79444040/17e655ea-e933-4e6b-8e97-721a51405353">
