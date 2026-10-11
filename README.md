# Spring Boot 영화 관리 Web UI

Spring Boot와 Thymeleaf를 이용하여 구현한 메모리 기반 영화 관리 프로그램.
기존 REST CRUD 프로젝트 구조를 유지하면서 영화 목록 조회 화면과 영화 등록 Form html과 상호작용을 위한 thymeleaf내용 추가.

## 1. 개발 환경

- Java 17
- Spring Boot 4.1.1
- Gradle
- Spring Web MVC
- Thymeleaf
- HTML
- CSS

## 2. 프로젝트 주제

영화 정보를 등록하고 조회할 수 있는 영화 관리 프로그램입니다.

영화는 다음 데이터로 구성됩니다.

| 필드 | 자료형 | 설명 |
|---|---|---|
| `id` | `Long` | 영화 식별 번호 |
| `title` | `String` | 영화 제목 |
| `director` | `String` | 감독 |
| `rating` | `int` | 평점 |
| `pubyear` | `int` | 개봉 연도 |
| `category` | `String` | 영화 카테고리 |

`id`는 영화 데이터가 저장될 때 메모리 저장소에서 자동으로 생성됩니다.

## 3. 주요 기능

### 3.1 영화 목록 조회

저장된 영화 목록을 HTML 테이블로 출력합니다.

```text
GET /movies
```

Controller가 Service를 통해 전체 영화 목록을 조회한 후 `Model`에 저장합니다.

```java
model.addAttribute("movies", movieService.findAll());
```

Thymeleaf는 Model에 저장된 `movies` 데이터를 반복하여 화면에 출력합니다.

```html
<tr th:each="movie : ${movies}">
```

### 3.2 영화 등록 화면

새로운 영화 정보를 입력할 수 있는 HTML Form을 제공합니다.

```text
GET /movies/new
```

입력 항목은 다음과 같습니다.

- 제목
- 감독
- 평점
- 개봉 연도
- 카테고리

### 3.3 영화 등록

HTML Form에 입력된 데이터를 서버로 전달하여 메모리 저장소에 저장합니다.

```text
POST /movies
```

Form의 `name`과 `MovieRequest`의 필드명을 일치시켜 `@ModelAttribute`로 데이터를 전달받습니다.

```text
name="title"     → title
name="director"  → director
name="rating"    → rating
name="pubyear"   → pubyear
name="category"  → category
```

Controller에서는 다음과 같이 데이터를 처리합니다.

```java
@PostMapping
public String create(@ModelAttribute MovieRequest request) {
    movieService.create(request);
    return "redirect:/movies";
}
```

### 3.4 PRG 패턴

영화 등록 후 목록 페이지로 Redirect하도록 구현했습니다.

```text
POST /movies
→ 영화 데이터 저장
→ redirect:/movies
→ GET /movies
→ 영화 목록 출력
```

등록 후 브라우저를 새로고침해도 같은 데이터가 다시 등록되지 않도록 PRG(Post-Redirect-Get) 패턴을 적용했습니다.

### 3.5 기존 REST API 유지

기존의 `MovieController`를 유지하여 JSON 형식의 REST API도 함께 사용할 수 있습니다.

```text
/api/movies
```

Web UI와 REST API는 서로 다른 URL을 사용합니다.

| 구분 | 기본 URL | 반환 형식 |
|---|---|---|
| REST API | `/api/movies` | JSON |
| Web UI | `/movies` | HTML |

## 4. 계층 구조

프로젝트는 Layered Architecture 구조로 구성했습니다.

```text
MovieWebController
        ↓
MovieService
        ↓
MovieRepository
        ↓
MemoryMovieRepository
        ↓
Map<Long, Movie>
```

각 계층의 역할은 다음과 같습니다.

### Controller

브라우저의 HTTP 요청을 처리합니다.

- 영화 목록 화면 반환
- 영화 등록 화면 반환
- Form 데이터 전달
- 등록 완료 후 Redirect

### Service

Controller와 Repository 사이에서 영화 관련 비즈니스 로직을 처리합니다.

### Repository

영화 데이터의 저장과 조회 기능을 정의합니다.

### MemoryMovieRepository

`Map<Long, Movie>`를 사용하여 영화 데이터를 메모리에 저장합니다.

### Thymeleaf

Controller가 Model에 저장한 데이터를 사용하여 HTML 화면을 생성합니다.

## 5. 프로젝트 구조

```text
src
├── main
│   ├── java
│   │   └── org.example.webui.week6_sb_crud_web
│   │       ├── controller
│   │       │   ├── MovieController.java
│   │       │   └── MovieWebController.java
│   │       ├── domain
│   │       │   └── Movie.java
│   │       ├── dto
│   │       │   ├── MovieRequest.java
│   │       │   └── MovieResponse.java
│   │       ├── repository
│   │       │   ├── MovieRepository.java
│   │       │   └── MemoryMovieRepository.java
│   │       ├── service
│   │       │   └── MovieService.java
│   │       └── Week6SbCrudWebApplication.java
│   │
│   └── resources
│       ├── templates
│       │   └── movies
│       │       ├── list.html
│       │       └── form.html
│       ├── static
│       │   └── css
│       │       └── style.css
│       └── application.properties
│
└── test
```

## 6. 목록 조회 실행 흐름

```text
Browser
→ GET /movies
→ MovieWebController
→ MovieService
→ MovieRepository
→ MemoryMovieRepository
→ List<MovieResponse>
→ Model
→ Thymeleaf
→ list.html
```

1. 브라우저가 `GET /movies`를 요청합니다.
2. `MovieWebController`가 요청을 받습니다.
3. `MovieService`를 통해 전체 영화 목록을 조회합니다.
4. 조회한 목록을 `Model`에 `movies`라는 이름으로 저장합니다.
5. Thymeleaf가 `movies/list.html`을 처리합니다.
6. 생성된 HTML이 브라우저에 표시됩니다.

## 7. 영화 등록 실행 흐름

```text
form.html
→ POST /movies
→ MovieWebController
→ @ModelAttribute MovieRequest
→ MovieService
→ MovieRepository
→ MemoryMovieRepository
→ redirect:/movies
→ GET /movies
→ list.html
```

1. 사용자가 영화 등록 Form에 값을 입력합니다.
2. 등록 버튼을 누르면 `POST /movies` 요청이 발생합니다.
3. Spring MVC가 입력값을 `MovieRequest`에 연결합니다.
4. `MovieService`가 영화 등록 작업을 처리합니다.
5. `MemoryMovieRepository`가 영화 데이터를 `Map`에 저장합니다.
6. Controller가 `redirect:/movies`를 반환합니다.
7. 브라우저가 `GET /movies`를 새로 요청합니다.
8. 등록된 영화가 목록에 출력됩니다.

## 8. Web UI URL

| HTTP 메서드 | URL | 기능 |
|---|---|---|
| `GET` | `/movies` | 전체 영화 목록 화면 |
| `GET` | `/movies/new` | 영화 등록 Form |
| `POST` | `/movies` | 새로운 영화 등록 |

## 9. 실행 방법

### 9.1 프로젝트 실행

프로젝트 최상위 폴더에서 다음 명령을 실행합니다.

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

애플리케이션이 정상적으로 실행되면 기본적으로 `8080` 포트를 사용합니다.

### 9.2 영화 목록 접속

브라우저에서 다음 주소로 접속합니다.

```text
http://localhost:8080/movies
```

### 9.3 영화 등록 화면 접속

```text
http://localhost:8080/movies/new
```

### 9.4 REST API 확인

```text
http://localhost:8080/api/movies
```

## 10. Thymeleaf 사용

### `th:each`

영화 목록을 반복합니다.

```html
<tr th:each="movie : ${movies}">
```

### `th:text`

영화의 각 필드를 HTML에 출력합니다.

```html
<td th:text="${movie.title}"></td>
```

### `th:href`

영화 등록 화면으로 이동하는 링크를 생성합니다.

```html
<a th:href="@{/movies/new}">새 영화 등록</a>
```

### `th:action`

Form 데이터를 전송할 URL을 설정합니다.

```html
<form th:action="@{/movies}" method="post">
```

## 11. 데이터 저장 방식

이 프로젝트는 데이터베이스를 사용하지 않습니다.

영화 데이터는 `MemoryMovieRepository`의 Java Collection에 저장됩니다.

```text
Map<Long, Movie>
```

따라서 애플리케이션을 종료하면 저장된 영화 데이터가 사라집니다. 애플리케이션을 다시 실행하면 빈 저장소에서 시작합니다.

## 12. 구현 확인

다음 기능의 동작을 확인했습니다.

- Spring Boot 애플리케이션 실행
- 영화 목록 페이지 출력
- 영화 등록 Form 출력
- Form 데이터와 `MovieRequest` 연결
- 영화 데이터 메모리 저장
- 등록 후 영화 목록으로 Redirect
- 등록한 영화의 목록 출력
- 새로고침 시 중복 등록 방지
- Thymeleaf를 이용한 데이터 반복 출력
- CSS를 이용한 화면 스타일 적용
- 기존 REST API 유지

## 13. 학습 내용

이 프로젝트를 통해 다음 내용을 학습했습니다.

- `@Controller`와 `@RestController`의 차이
- Controller, Service, Repository의 역할
- Spring MVC의 `Model`
- Thymeleaf의 기본 문법
- HTML Form과 DTO의 데이터 연결
- `@ModelAttribute`의 동작 방식
- PRG(Post-Redirect-Get) 패턴
- Java Collection을 이용한 메모리 저장
- 정적 CSS 파일을 Thymeleaf 화면에 연결하는 방법