## 📝 Tanylog
#### 💁🏻 `프로젝트명` Tanylog API
#### 📆 `기간` 2023.07.26 ~ 2023.09.07
#### 📝 [한 눈에 보는 프로젝트 Notion 문서](https://concrete-blanket-9f9.notion.site/API-dc1a77668287447fa62575f273720631?pvs=4)
#### 📝 [Spring Rest Docs](http://15.165.146.174/docs/index.html)

<br>

### 💁🏻 기능 구현
---
**Back-end**

- 로그인 구현 **[(포스팅)](https://www.notion.so/OAuth2-0-a63bef8660124f729b0e702e5388d35c?pvs=21)**
    - Spring Security + OAuth2 + Session, Cookie 방식 로그인 구현
</br>

- API 명세서 작성 **[(포스팅)](https://www.notion.so/Spring-Rest-Docs-API-7e692625f8fc4faeb46225718a4f9b8a?pvs=21)**
    - Spring Rest Docs 를 활용해 API 명세서 작성
</br>

- 게시글 CRUD API **[(포스팅)](https://www.notion.so/CRUD-c116ae9be9ea497f80a02a181a80b8fb?pvs=21)**
    - 게시글 전체 조회
        - OFFSET 방식으로 Paging 처리되도록 구현
        - Querydsl 을 활용해 Paging, Search 값이 동적으로 활용될 수 있도록 구현 **[(포스팅)](https://www.notion.so/JPQL-Querydsl-Paging-c5a5bececf7a4fba848db18c885494f0?pvs=21)**
    - 내가 작성한 게시글 전체 조회
        - 다른 조회 API 와는 다르게 비로그인 상태에서 API 호출 시 로그인 페이지로 Redirect 되도록 구현
    - 게시글 삭제
        - 게시글 삭제 시 Soft Delete 되도록 구현
    - 게시글 검색
        - 제목, 내용, 닉네임으로 게시글을 검색할 수 있도록 구현
</br>

- 댓글 CRUD API **[(포스팅)](https://www.notion.so/CRUD-aa8268333bba4ea18732577ac6751c97?pvs=21)**
    - 댓글 조회
        - 한 게시물의 상위 댓글 조회 시 NO OFFSET 방식으로 Paging 처리되도록 구현
        - 상위 댓글의 하위 댓글 목록은 OFFSET 방식으로 Paging 처리되도록 구현
    - 댓글, 대댓글 등록
        - 대댓글은 하나 이상 등록하지 못하도록 구현
</br>

- 게시글 좋아요 API **[(포스팅)](https://www.notion.so/API-8c4997d53eb84635a147a364341a62fc?pvs=21)**
    - API 호출 시 한 유저가 동시 요청을 보내는 상황 제어할 수 있도록 구현 **[(포스팅)](https://www.notion.so/cc55c11e313c47569165a0a7993f1a65?pvs=21)**
- Junit5 를 활용해 Controller, Service 테스트 코드 작성
    - 게시글
        [게시글 CRUD Controller 테스트 작성](https://www.notion.so/CRUD-Controller-cdc4634a34c94383af7b20624b158d8f?pvs=21)
        [게시글 CRUD Service 테스트 작성](https://www.notion.so/CRUD-Service-390e12e9dbea435e84666bbbd25446be?pvs=21)
        
    - 댓글
        [댓글 CRUD Controller 테스트 작성](https://www.notion.so/CRUD-Controller-b23756a17d9846b9acb146aedd158ca1?pvs=21)
        [댓글 CRUD Service 테스트 작성](https://www.notion.so/CRUD-Service-a33f890951ff42deb41191f85248fcbe?pvs=21)
        [댓글 CRUD Service 테스트 작성](https://www.notion.so/CRUD-Service-1a22eb27fc88491182beb01a90f761b8?pvs=21)
</br>

<details>
<summary> nGrinder 를 활용해 API 성능 테스트 실시 </summary>
<div>
    
- 커버링 인덱스를 적용해 성능 개선
- 약 3배의 성능 개선 성공
- 성능 개선 전 10 분간 성능 테스트 
    - 가상 유저 99 명 기준 
    - 평균 테스트 시간 20 초 
    - 최고 TPS 8
    - <img width="1028" alt="성능 개선 전 엔그라인더" src="https://github.com/juni8453/Tanylog/assets/79444040/e695d198-4267-4f58-a1a9-284657a062a2">
     
- 성능 개선 후 10 분간 성능 테스트
    - 가상 유저 99 명 기준
    - 평균 테스트 시간 9.5 초
    - 최고 TPS 14 
    - <img width="1335" alt="성능 개선 후 엔 그라인더" src="https://github.com/juni8453/Tanylog/assets/79444040/83af80b1-9573-492f-a850-d16a6eccc6da">

- 성능 개선 전 Postman 기준 속도 측정
    - 응답 시간 약 1.3 초
    - <img width="1014" alt="스크린샷 2023-09-09 오후 3 45 29" src="https://github.com/juni8453/Tanylog/assets/79444040/3e15a380-dd22-494f-9c37-4165ae336b6e">
- 성능 개선 후 Postman 기준 속도 측정
    - 응답 시간 약 0.4 초     
    - <img width="1009" alt="스크린샷 2023-09-09 오후 3 46 33" src="https://github.com/juni8453/Tanylog/assets/79444040/62cd8410-8e02-4a11-a89a-087ce75d8d22">

</div>
</details>
</br>

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
