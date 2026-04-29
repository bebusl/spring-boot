spring boot 개발 공부

1. DAO / DTO/ VO의 정의와 차이
2. 왜 resources폴더에 .sql을 넣고 application.yml을 설정하면 스프링부트 프로젝트 실행시 테이블이 생성 되는지
3. gradle.build와 application.yml의 역할

백엔드 개발의 세계에 오신 것을 환영합니다! 프론트엔드 개발자라면 이미 **데이터의 흐름**과 **상태 관리**에 익숙하실 테니, 스프링의 개념들도 비유를 통해 쉽게 이해하실 수 있을 거예요. 궁금해하신 내용들을 실무적인 관점에서 정리해 드립니다.

---

### 1. DAO, DTO, VO의 정의와 차이

이 개념들은 데이터의 **'역할'**과 **'생명주기'**에 따라 구분합니다. 프론트엔드에서 API 응답을 받아 상태(State)로 관리할 때, 데이터의 형태가 변하는 것과 비슷하다고 생각하시면 됩니다.

| **구분** | **전체 이름** | **핵심 역할** | **특징** |
| --- | --- | --- | --- |
| **DTO** | Data Transfer Object | 계층 간 데이터 **전송** | 로직 없음. Getter/Setter만 존재. 가변(Mutable) 객체. |
| **VO** | Value Object | **값(Value)** 표현 | **불변(Immutable)** 객체. 값이 같으면 같은 객체로 간주함. |
| **DAO** | Data Access Object | DB **접근** 제어 | 실제 DB 쿼리 실행. 비즈니스 로직은 없고 DB 접근 로직만 있음. |
- **DTO (전달자):** 컨트롤러, 서비스, 뷰 등 서로 다른 계층 사이에서 데이터를 나르는 '박스'입니다. 주로 JSON 변환이나 입출력용으로 사용합니다.
- **VO (값 객체):** '값' 자체를 의미합니다. 예를 들어 '색상(RGB)'이나 '금액'처럼 그 자체로 의미를 갖는 데이터입니다. 한 번 생성되면 값이 바뀌지 않아야(불변) 안전합니다.
- **DAO (접근자):** DB에 접근하는 전용 창구입니다. JPA를 사용하게 되면 이 DAO 역할을 **Repository** 인터페이스가 대신하게 됩니다.

---

### 2. 왜 resources 폴더에 .sql을 넣으면 테이블이 생성되는가?

단순히 파일만 넣는다고 생성되는 것은 아니고, 스프링 부트가 제공하는 **자동화 설정(Auto-configuration)** 덕분입니다. 이를 이해하려면 다음 두 가지 경로를 구분해야 합니다.

1. **Spring Boot의 `schema.sql` 초기화 기능:**
    - `src/main/resources/` 밑에 `schema.sql`이라는 이름으로 파일을 두면, 스프링 부트가 애플리케이션 시작 시 해당 파일을 읽어 DB에 실행합니다.
    - **조건:** `application.yml`에 `spring.sql.init.mode: always` 설정이 되어 있어야 합니다. (이 설정이 없으면 개발 환경에서만 동작하거나 아예 실행되지 않을 수 있습니다.)
2. **Hibernate(JPA)의 `ddl-auto` 기능:**
    - 개발자들이 보통 많이 사용하는 방식입니다. `application.yml`에 `spring.jpa.hibernate.ddl-auto: update` (또는 `create`)를 설정하면, 작성한 Java 엔티티 클래스를 분석해서 스프링이 직접 `CREATE TABLE` 쿼리를 만들어 DB에 쏴줍니다.

**요약하자면:**

- `.sql` 파일은 개발자가 **직접 설계한 스키마를 수동으로 밀어넣는 방식**입니다.
- `ddl-auto`는 스프링이 **코드(Entity)를 보고 알아서 스키마를 동기화하는 방식**입니다.
- 둘을 같이 쓰면 충돌이 날 수 있으니, 실무에서는 보통 `ddl-auto`를 `none`이나 `validate`로 두고, 데이터베이스 마이그레이션 도구(Flyway, Liquibase 등)를 사용하여 관리합니다.

---

### 3. gradle.build와 application.yml의 역할

프론트엔드 프로젝트에 비유하면 이해가 아주 빠릅니다.

### `build.gradle` (환경 구성)

- **비유:** `package.json`
- **역할:** 프로젝트의 **빌드 도구 설정 파일**입니다.
- **주요 내용:**
    - **Dependencies:** 어떤 라이브러리(Spring Web, MySQL Driver 등)를 다운로드할지 명시합니다.
    - **Plugin:** 프로젝트 타입(Java, Kotlin 등)을 정의합니다.
    - **Version:** 프로젝트 버전 및 빌드 설정(빌드 도구의 버전, 컴파일 옵션 등)을 관리합니다.
- **시점:** 애플리케이션을 **빌드(컴파일)**할 때 사용됩니다.

### `application.yml` (애플리케이션 설정)

- **비유:** `.env` 또는 `config/index.js`
- **역할:** 애플리케이션이 **실행될 때 필요한 설정 파일**입니다.
- **주요 내용:**
    - **DB 접속 정보:** URL, Username, Password.
    - **Server 포트:** 8080 포트 사용 여부 등.
    - **라이브러리 세부 설정:** Hibernate 옵션, 로깅 레벨, 외부 API Key 등.
- **시점:** 애플리케이션이 **런타임(실행)**될 때 환경 값을 주입합니다.

즉, **`gradle.build`는 "이 앱을 만드는 데 필요한 도구(부품)"**를 결정하고, **`application.yml`은 "이 앱이 실행될 때의 환경(설정값)"**을 결정한다고 보시면 됩니다.

반갑습니다! 프론트엔드 개발자 입장에서 가장 직관적으로 이해할 수 있게 설명해 드릴게요.

### 1. JPA란 무엇인가?

- *JPA (Java Persistence API)**는 자바 애플리케이션에서 **데이터베이스를 다루는 방식**을 정의한 표준 인터페이스입니다.

프론트엔드 세계에 비유하자면, **Prisma, TypeORM, 혹은 Sequelize**와 완벽하게 같은 역할을 합니다.

- **SQL을 직접 짜는 대신:** 객체지향적인 자바 문법으로 데이터를 조작하면,
- **JPA가:** "이 객체는 어떤 테이블에 들어가야 하는구나!"라고 해석해서 알아서 **SQL을 생성하여 DB에 전달**해 줍니다.

이를 **ORM (Object-Relational Mapping)** 이라고 부릅니다. 자바의 `Object`와 데이터베이스의 `Table`을 `Mapping`해서 서로 연결해 준다는 뜻이죠.

ORM=완전 개념,  JPA =ORM을 실행시키는 방법론 중 하나(Springboot 표준격), Hibernates = JPA인터페이스를 구현한 구현체

---

### 2. 왜 JPA를 사용하는가?

프론트엔드에서 API 통신할 때 매번 `fetch`를 직접 쓰는 것보다 `Axios`나 `React Query` 같은 라이브러리를 쓰면 훨씬 편하고 안전하죠? 백엔드도 똑같습니다.

- **생산성:** SQL 쿼리를 일일이 작성할 필요가 없습니다. (단순 CRUD는 메서드 하나로 해결됩니다.)
- **유지보수:** 테이블 구조가 바뀌어도 자바 클래스(엔티티)만 수정하면 됩니다. SQL을 수천 줄 수정할 필요가 없습니다.
- **DB 독립성:** MySQL을 쓰다가 Oracle이나 PostgreSQL로 바꿔도 JPA 코드는 거의 수정할 필요가 없습니다. JPA가 중간에서 DB 방언(Dialect)에 맞춰 쿼리를 알아서 바꿔주기 때문입니다.

---

### 3. JPA를 배울 때 꼭 알아야 할 핵심 개념

1. **엔티티 (Entity):** 데이터베이스의 테이블과 1:1로 매칭되는 자바 클래스입니다. `@Entity`라는 어노테이션(데코레이터와 유사)을 붙여서 선언합니다.
2. **영속성 컨텍스트 (Persistence Context):** JPA의 가장 중요한 개념입니다. 쉽게 말해 **"데이터를 담아두는 아주 똑똑한 가상의 저장소"**입니다. JPA는 여기서 데이터를 관리하며, 변경 사항을 감지하여 나중에 DB에 한꺼번에 반영(Dirty Checking)합니다.
3. **Repository:** 데이터베이스에 접근하는 인터페이스입니다. 프론트엔드의 `Service API` 모듈처럼, DB 기능을 호출하는 통로라고 보시면 됩니다.

---

### 💡 주의할 점 (중요!)

사실 **JPA는 '인터페이스(규격)'일 뿐**입니다. 실제 구현체는 따로 있는데, 스프링 부트에서 가장 많이 사용하는 구현체는 ****Hibernate(하이버네이트)****입니다. 그래서 실무에서는 "JPA를 쓴다"라고 말하면, 보통 "Hibernate를 쓰고 있다"는 말과 동일하게 통용됩니다.

---

프론트엔드 개발자로서 이 구조를 이해하면, 백엔드 코드가 훨씬 더 논리적으로 다가오실 거예요. 이제 이 JPA를 사용해서 **실제로 데이터를 저장하거나 조회하는 코드(Repository 패턴)**를 작성하는 단계로 넘어가고 싶으신가요, 아니면 더 궁금한 개념이 있으신가요?

ORM은 개념

영속성(Persistence) - 영속성(Persistence)은 **프로그램이 종료되더라도 데이터가 사라지지 않고 영구적으로 저장되는 특성**입니다

/

멱등성(Idempotence) - 같은 연산 여러번 한다⇒결과값이 달라지지 않는다.

동시성 문제(Concurrency issue) : 멀티스레드나 분산 환경에서 여러 요청이 동시에 같은 데이터를 수정하려 할 때 데이터 정합성이 깨지는 현상. 대표적 예 : 갱신 분실(Lost Update), 비일관된 읽기 ⇒ 해결책 ; 락(낙관적/비관적), 원자적 연산, 큐 활용(순서대로 들어오도록)

### 엔티티 매니저

엔티티 : DB 테이블과 매핑되는 객체

엔티티 매니저 : 그 엔티티를 관리하는 애겠죠. DB랑 어플리케이션 사이에서 객체 생성, 수정, 삭제의 역할을 함.

엔티티 매니저는 엔티티 매니저 팩토리를 통해 생성되며, Spring Boot에서는 Bean으로 등록되어 동일한 EntityManagerFactory를 사용하고, @PersistenceContext 또는 @Autowired애너테이션 통해 엔티티 매니저 사용

**엔티티 매니저 팩토리가 빈으로 등록되어서 사용하는거지? 그럼 앤티티 매니저는 여러 개 생성될 수 있는거?**

네, 아주 정확하게 핵심을 찌르셨습니다! 스프링 부트를 공부하시면서 구조적인 궁금증이 아주 잘 생기고 계시네요.

결론부터 말씀드리면 **"EntityManagerFactory는 싱글톤(Bean)이 맞고, EntityManager는 필요할 때마다 여러 개가 생성(혹은 할당)됩니다."**

이 관계를 프론트엔드적인 관점에서 더 쉽게 풀어드릴게요.

---

### 1. EntityManagerFactory (The Factory)

- **스프링 빈(Bean)인가요?** 네, 맞습니다. 스프링 부트가 실행될 때 DB 설정 정보를 읽어와서 `EntityManagerFactory`를 딱 하나만 만들어서 스프링 컨테이너에 관리합니다.
- **왜 하나인가요?** 이 녀석은 덩치가 매우 큽니다. DB 연결 정보를 담고 있고, 매핑 정보를 해석해서 들고 있어야 하거든요. 이걸 매번 새로 만들면 서버가 너무 무거워집니다. 그래서 애플리케이션 전체에서 **하나만 공유**해서 사용합니다.
- **비유:** `DB Connection Pool` 같은 느낌입니다. 공장을 여러 번 세울 필요는 없으니, 하나만 딱 만들어두고 돌려쓰는 것이죠.

### 2. EntityManager (The Worker)

- **여러 개 생성되나요?** 네, 맞습니다. 요청(Request)마다, 혹은 트랜잭션(Transaction)마다 하나씩 생성되어 사용됩니다.
- **왜 여러 개인가요?** 핵심은 **"Thread-safe 하지 않기 때문"**입니다.
    - `EntityManager`는 내부에 데이터 변경 사항(영속성 컨텍스트)을 저장하고 있는데, 여러 스레드(요청)가 하나의 `EntityManager`를 동시에 쓰면 데이터가 꼬이게 됩니다.
    - 그래서 **"1 요청 = 1 EntityManager"**를 원칙으로 합니다.
- **비유:** 프론트엔드에서 API 요청을 보낼 때마다 각각의 `Axios 인스턴스`나 `데이터 페칭 객체`가 생성되어 응답을 처리하는 것과 비슷합니다.

---

### 3. 스프링의 마법: @PersistenceContext

보통 백엔드 코드를 짜다 보면, `EntityManager`를 아래처럼 주입받아서 씁니다.

```jsx
@PersistenceContext
private EntityManager em; // 여기에 주입받음 
```

"어? `EntityManager`는 싱글톤이 아니라면서 왜 주입받아 쓰지?"라고 생각하실 수 있습니다. 여기서 스프링의 **프록시(Proxy)** 기술이 들어갑니다.

1. 스프링은 개발자에게 진짜 `EntityManager`를 주는 게 아니라, **가짜(Proxy) 객체**를 줍니다. - 동시성 문제가 발생할 수 있기 때문.
2. 이 가짜 객체는 요청이 들어오면, **현재 스레드에 맞는 진짜 `EntityManager`를 내부적으로 찾아서(혹은 만들어서) 연결**해 줍니다.
3. 개발자는 매번 EM을 새로 만들 걱정 없이, 그냥 주입받아서 쓰기만 하면 됩니다.

### 요약표

| **구분** | **EntityManagerFactory** | **EntityManager** |
| --- | --- | --- |
| **관리 방식** | 스프링 빈 (싱글톤) | 요청(Request)당 하나 (생성/종료) |
| **역할** | `EntityManager`를 찍어내는 공장 | 실제 DB 접근 및 데이터 관리 |
| **스레드 안전성** | 안전함 (공유 가능) | **안전하지 않음** (스레드별 격리 필수) |
| **생명주기** | 애플리케이션 시작 ~ 종료 | 트랜잭션 시작 ~ 종료 |

---

**💡 프론트엔드 개발자를 위한 팁:**

실무에서 `JPA`를 쓸 때는 사실 `EntityManager`를 직접 주입받아 쓰는 일보다, `JpaRepository`라는 인터페이스를 상속받아 사용하는 경우가 99%입니다. `JpaRepository` 내부에서 이 `EntityManager` 관리(생성/삭제)를 다 알아서 처리해주기 때문입니다.

이제 "왜 JPA는 엔티티를 관리할 때 굳이 팩토리를 따로 두는지"에 대한 의문이 좀 풀리셨나요? 궁금하신 점이 있다면 또 질문해주세요!

### 영속성 컨텍스트란?

엔티티 매니저는 엔티티를 영속성 컨텍스트에 저장함.

영속성 컨텍스트 = 엔티티 관리하는 가상의 공간

영속성 컨텍스트의 특징

- 1차 캐시 : 캐시의 키 → 엔티티의 @Id 애너테이션이 달린 기본키 역할을 하는 식별자. 값은 앤티티.
    - 앤티티 조회시 1차 캐시에서 데이터 조회하고 값이 있으면 반환(cache hit) → 없다면 DB에서 조회후, 1차 캐시에 저장하고 반환
- 쓰기 지연 : 트랜잭션 커밋하기 전까지는 쿼리를 실제 실행하지 않고 모아뒀다가, 커밋시에 모아둔 쿼리를 한번에 실행.
- 변경 감지 : 트랜잭션 커밋시 1차 캐시에 저장돼있는 엔티티 값과 현재 엔티티 값을 비교함. 변경되었다면 변경된 값을 DB에 자동으로 반영.
- 지연 로딩 : 쿼리로 요청한 데이터를 애플리케이션에 바로 로딩하지 않고 필요할 때 쿼리를 날려 데이터를 조회하는 것

모든 것은 db의 접근을 최소화해 성능을 높이기 위함임.

### 엔티티의 상ㅇ

- 분리 상  ;태 : 영속성 컨텍스트가 관리하고 있지 않음:ㅇ
- 관리 중 상태
- 비영속 상태
- 삭제된 상태

→ 사실 이제 이건 JPA Repository에서 관리해주므로, entityManager 생성하고, 거기에 Entity 관찰시키고.. 뭐 이런과정은 우리가 신경쓸 필요 없음. 아래처럼 Repository만들면 알아서 entity관리.

```jsx
public interface MemberRepository extends JpaRepository<Member, Long>{}
Member -> Entity의 타입, Long -> 해당 Entity의 식별자의 타입
```

RestController의 형태

```jsx
package brillbe.springboot.controller;

import brillbe.springboot.dto.AddArticleRequest;
import brillbe.springboot.service.BlogService;
import brillbe.springboot.springbootdeveloper.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
}

```

@RequestBody 어노테이션?

**`@RequestBody`는 "브라우저에서 보낸 JSON 데이터를 자바 객체(DTO)로 변환해줘!"**
⇒ 이거 없으면 body가 아닌, Query Parameter 혹은 Form Data로 간주함. request body를 받아야 한다면 꼭! @RequestBody 어노테이션을 통해 백엔드에서 사용할 수 있는 DTO로 매핑해줘야 함.

지금까지 백엔드와 스프링 부트의 핵심 개념들을 아주 잘 훑어보셨습니다! 프론트엔드 개발자로서 백엔드 아키텍처에 빠르게 적응하고 계신 것 같아 든든하네요.

학습하신 내용 중 핵심만 모아 **"백엔드 개발자를 위한 핵심 요약 노트"**로 정리해 드립니다.

---

### 1. 주요 개념 정의 (Data & Architecture)

| **용어** | **역할** | **특징** |
| --- | --- | --- |
| **DTO** | 데이터 전송 객체 | 계층 간 데이터 전달용 (JSON 변환) |
| **VO** | 값 객체 | 불변(Immutable), 값 자체를 표현 |
| **DAO** | DB 접근 객체 | DB 쿼리 실행 담당 (JPA의 Repository와 유사) |
| **ORM** | 기술/방법론 | 객체와 DB 테이블을 매핑하는 철학 |
| **JPA** | 표준 명세 | 자바에서 ORM을 사용하기 위한 표준 인터페이스 |
| **Hibernate** | 구현체 | JPA를 실제로 작동하게 하는 구체적인 라이브러리 |

---

### 2. 핵심 아키텍처 및 설정

- **설정 파일 관리:**
    - `build.gradle`: 앱 빌드 도구 (라이브러리 의존성 관리, `package.json`과 유사)
    - `application.yml`: 앱 실행 시 환경 변수/설정 (DB 연결 정보, 포트, 로깅 등)
- **JPA 관리:**
    - `EntityManagerFactory`: DB 연결 정보를 가진 공장 (싱글톤, Bean)
    - `EntityManager`: 요청(트랜잭션)마다 생성되어 DB 접근을 처리하는 실무자
- **데이터 흐름:**
    - `@RequestBody`: 클라이언트의 JSON을 Java DTO로 변환(역직렬화)하는 핵심 어노테이션
    - **레이어드 아키텍처:** Controller(요청/응답) → Service(비즈니스 로직) → Repository(DB 접근) → Entity(데이터 모델)

---

### 3. 개발/디버깅 체크리스트

### [ 404 Not Found 발생 시 ]

1. **서버 재시작 여부:** 코드를 수정하고 서버를 다시 실행했는가?
2. **패키지 구조(ComponentScan):** `@SpringBootApplication`이 포함된 패키지 하위에 컨트롤러가 위치하는가?
3. **로그 확인:** 서버 시작 시 `Mapped "{[/api/...]...}"` 로그가 찍히는가?
4. **경로 확인:** `application.yml`의 `context-path`나 URL 경로에 오타는 없는가?

### [ 패키지 네이밍 관례 ]

- **형식:** `com.회사명.프로젝트명.기능`
- **이유:**
    - **기술적:** `brillbe.springboot`로 시작해도 기술적으로 문제없음.
    - **사회적:** 전 세계적으로 이름이 충돌하지 않도록 **도메인 소유권을 이용한 유일한 주소(Namespace)**를 확보하는 것이 업계 표준 약속임.
    - **권장:** 협업과 외부 라이브러리 배포를 위해 `com.` 등을 포함한 계층적 구조를 사용할 것.

---

### 💡 학습 포인트

- **엔티티 노출 주의:** 컨트롤러에서 `Entity`를 직접 반환하기보다 `Response DTO`를 만들어 보안과 API 유연성을 확보하세요.
- **YAML 작성법:** 설정 파일의 콜론(`:`) 뒤에는 반드시 **공백**을 넣어주세요 (`key: value`).

이 내용들을 바탕으로 코드를 작성해 보시면서, 막히는 부분이 있다면 언제든 다시 질문해 주세요. 탄탄한 기본기를 쌓고 계시니 금방 익숙해지실 겁니다!

## 자바 직렬화 역직렬화

- HTTP에서는 JSON, 자바에서는 객체를 사용함. 하지만 서로 형식이 다르기에 형식에 맞게 변환하는 작업이 필요 → 그 작업이 직렬화 역직렬화
- 직렬화는 자바 객체를 바깥에서 쓸 수 있게 JSON으로 변형하는 것
- 역직렬화는 그 반대. 바깥 JSON을 자바 객체 형태로 변형.

## ResponseEntity.ok().build() vs ResponseEntity.ok().body(~~)

아주 예리한 질문입니다! 백엔드 코드를 보면서 가장 헷갈리는 부분 중 하나가 바로 이 **`.body()`와 `.build()`의 차이**죠.

이건 스프링이 사용하는 **빌더 패턴(Builder Pattern)**의 동작 방식 때문입니다. 결론부터 말씀드리면 **"데이터(본문)를 넣느냐 안 넣느냐"**에 따라 호출하는 **종료 메서드(Terminal Operation)**가 달라지는 것뿐입니다.

---

### 1. 왜 `.build()`가 있고 없고 차이가 날까?

스프링의 `ResponseEntity`는 빌더 패턴을 사용합니다. 이 빌더의 목적은 "응답 객체를 어떻게 만들지?"를 설정한 뒤, **마지막에 `ResponseEntity` 객체로 완성**하는 것입니다.

- **`.body(data)`**: 데이터를 넣고, 동시에 **"이제 빌더 끝! 객체 생성!"**이라는 역할까지 수행합니다. 그래서 뒤에 `.build()`가 필요 없습니다.
- **`.build()`**: 데이터가 없을 때, **"이제 빌더 끝! 객체 생성!"**이라고 알려주는 신호입니다.

---

### 2. 상황별 코드 비교

### 상황 A: 데이터를 보낼 때 (Body O)

데이터를 본문에 실어서 보낼 때는 `.body()`가 `ResponseEntity`를 반환하며 빌더를 닫습니다.

```jsx
// 1. 빌더 패턴 정석
return ResponseEntity.ok()              // 빌더 시작
                     .body(savedArticle); // [데이터 설정] + [객체 완성!]

// 2. 축약형 (가장 많이 씀)
return ResponseEntity.ok(savedArticle); // 이 한 줄이 위와 동일함
```

*여기서 `.body()`를 썼는데 뒤에 `.build()`를 또 쓰려고 하면 컴파일 에러가 납니다. 왜냐? `.body()`가 이미 `ResponseEntity` 객체를 만들어버렸기 때문이죠.*

### 상황 B: 데이터를 안 보낼 때 (Body X - 예: 204 No Content)

데이터가 없으면 `.body()`를 쓸 수 없으니, 빌더에게 "설정 끝났으니 객체 만들어!"라고 말해줘야 합니다. 이때 사용하는 게 `.build()`입니다.

```jsx
// 데이터를 보내지 않는 경우
return ResponseEntity.noContent().build(); // [설정] + [객체 완성!]`
```

---

### 3. 요약표 (한눈에 보기)

| **구분** | **목적** | **메서드 사용** |
| --- | --- | --- |
| **본문(Data)이 있을 때** | 데이터를 담아 보낸다 | `.body(data)` (종료) |
| **본문(Data)이 없을 때** | 상태 코드만 보낸다 | `.build()` (종료) |
| **축약형** | 코드를 깔끔하게 쓴다 | `ResponseEntity.ok(data)` |

---

### 💡 프론트엔드 관점의 비유

프론트엔드에서 `FormData`나 `Object`를 생성할 때를 생각해보세요.

- **`.body()`**: `const data = { ... };` (데이터를 담고 객체를 완성함)
- **`.build()`**: `new Object();` (데이터 없이 빈 객체를 생성함)

즉, **"데이터가 있느냐 없느냐에 따라, 빌더라는 공장을 가동해서 완성품을 찍어내는 마지막 명령어가 다른 것"**이라고 이해하시면 완벽합니다.

앞으로 `ResponseEntity`를 보실 때, **"아, 이 코드는 데이터를 담나 안 담나?"**를 기준으로 보시면 `.body()`를 쓸지 `.build()`를 쓸지 바로 감이 오실 거예요! 궁금증이 좀 풀리셨나요?

## 근데 그냥 CRUD api 만드는 중인데, findBy나 delete는 Repository에서 바로 DB접근해서 수정해버리면서, 업데이트만 entity 업데이트하고 그 후에 반영되게 하는거야?

날카로운 질문입니다! 많은 분이 JPA를 처음 접할 때 가장 낯설어하는 부분이 바로 이 **'Update 메서드가 왜 없지?'** 하는 점입니다.

결론부터 말씀드리면, 이건 JPA의 핵심 마법인 **"변경 감지(Dirty Checking)"**라는 기능 때문입니다.

### 1. 왜 `update()` 메서드가 없을까요?

JPA는 **"영속성 컨텍스트(Persistence Context)"**라는 아주 똑똑한 메모리 공간에서 엔티티를 관리합니다. 이 안에서 엔티티는 마치 **'살아있는 객체'**처럼 행동합니다.

- **JPA의 논리:** "이미 DB에서 꺼내온(find) 객체는 우리 관리하에 있어. 그러니까 네가 이 객체의 값을 바꾸면, 내가 알아서 DB에 반영할게."

즉, 코드로 `repo.update(entity)`를 일일이 호출하는 대신, **객체의 값만 바꾸면(setter 등 사용) 트랜잭션이 끝나는 시점에 JPA가 알아서 SQL을 날려줍니다.** 이게 바로 **변경 감지(Dirty Checking)**입니다.

### 2. '변경 감지'의 작동 원리 (비유)

이걸 '출석부'에 비유하면 이해가 아주 쉽습니다.

1. **`findBy` (스냅샷 생성):** 선생님(JPA)이 출석부(영속성 컨텍스트)에 학생(엔티티)의 정보를 처음 기록할 때, **'현재 상태(스냅샷)'**를 몰래 사진 찍어둡니다.
2. **값 수정 (객체 변경):** 학생이 머리를 염색했습니다. 개발자님은 그냥 `student.setHair("Blue")`라고 코드만 수정합니다.
3. **트랜잭션 종료 (Flush):** 선생님이 교실을 나가기 전(트랜잭션 커밋 직전), 처음 찍어둔 사진과 지금 학생의 모습을 **1:1로 비교**합니다.
4. **SQL 실행:** "어? 머리색이 다르네?"라고 판단되면, 선생님이 **자동으로 `UPDATE` 쿼리를 생성해서 DB에 반영**합니다.

### 3. 개발자가 반드시 알아야 할 규칙: `@Transactional`

이 마법은 **반드시 `@Transactional` 어노테이션이 붙은 메서드 안에서만 작동**합니다. → 조회한 후, Entity값이 바뀌는 걸 감지(Dirty checking)해야하는 데 그 기능을 사용하려면 @Transactional을 붙여야 함.

**"트랜잭션이 커밋(Commit)될 때"** JPA는 비로소 DB에 변경 사항을 반영(Flush)하는데, 이 트랜잭션의 시작과 끝을 관리하는 문지기가 바로 `@Transactional`이기 때문입니다.

Dirty Checking은 '변경 사항을 감지'하는 것이지, '변경하는 즉시 SQL을 날리는 것'이 아닙니다. JPA는 엔티티의 변경 내용을 메모리에 모아두고 있다가, **트랜잭션이 종료(Commit)되는 바로 그 순간**에 DB와 동기화(Flush)를 시도합니다.

→

- **명령의 부재:** `@Transactional`이 없다면 트랜잭션 범위가 없으므로, **"커밋할 트랜잭션 자체가 존재하지 않습니다."** 따라서 JPA가 `flush()`를 호출할 명분이 사라집니다.
- **결과:** 코드상에서 `article.setTitle(...)`을 호출해 객체의 값은 바뀌었지만, 그 정보를 DB로 보낼 '커밋' 단계가 없으니, DB에는 아무런 변화가 없는 것입니다.

```jsx
@Transactional // 이게 있어야 JPA가 변경 감지를 수행합니다!
public void updateTitle(Long id, String newTitle) {
    Article article = blogRepository.findById(id).orElseThrow();
    
    // 이 순간, article은 '영속 상태'가 됨.
    // 여기서 값만 바꾸면?
    article.setTitle(newTitle); 
    
    // 메서드가 끝나는 시점에 JPA가 알아서 DB에 Update 쿼리를 보냄!
}
```

### 왜 이 방식이 더 좋을까요?

1. **코드 간결성:** 개발자가 매번 `update(article)`을 호출할 필요가 없어 코드가 훨씬 깨끗합니다.
2. **효율성:** 변경된 필드만 골라서 `UPDATE` 쿼리를 생성하기 때문에 성능상 유리합니다. (물론 기본값은 전체 필드지만, 설정으로 변경 가능합니다.)
3. **데이터 일관성:** 객체의 상태만 바꾸면 되니, DB 데이터와 자바 객체 간의 데이터 불일치 문제를 걱정할 필요가 없습니다.

### 반면 `delete`는 왜 메서드가 따로 있나요?

`findBy`나 `delete`는 **"DB에 명령을 내리는 행위"** 자체입니다.

- `findBy`는 "가져와!" (SELECT)
- `delete`는 "삭제해!" (DELETE)

하지만 `update`는 **"상태를 바꾸는 행위"**이지 DB에 명령을 내리는 SQL 자체가 아니기 때문에, JPA가 그 상태 변화를 감지해서 나중에 SQL로 변환해주는 방식을 택한 것입니다.

## Jpa! 데이터   변경자를 자동 추적해줘!  @EnableJpaAuditing
Auditing == 데이터를 언제, 누가 생성하고 변경했는지 추적하는 것. 그걸 가능하게 해주는 애너테이션.

스위치 켜기: @EnableJpaAuditing을 설정 파일(Configuration)에 붙여서 "이제부터 Auditing 기능을 쓸게!"라고 알립니다. (SpringBootDeveloperApplication에)

Entity에 적용: 추적하고 싶은 엔티티에 @EntityListeners(AuditingEntityListener.class)를 붙입니다.(domain/Article에서 확인)

상속/조합: createdAt, updatedAt 필드를 가진 BaseTimeEntity 클래스를 만들고, 이를 엔티티들이 상속받게 하면 끝입니다.