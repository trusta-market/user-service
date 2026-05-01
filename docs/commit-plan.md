# Commit Plan

이 문서는 `user-service` 변경분을 한 번에 올리지 않고, 기능과 책임 경계가 섞이지 않도록 커밋을 나누는 기준을 정리한다.

## 분리 원칙

- 빌드/무시 규칙을 먼저 고정한다.
- 공통 브릿지 코드와 도메인 코드를 분리한다.
- 엔티티 생성, DTO 매핑, 컨트롤러 응답 변환은 각각 자기 책임 안에서만 수정한다.
- QueryDSL 저장소 구현은 마지막에 분리한다.
- `.idea`, `*.iml`, `build`, `.gradle` 같은 로컬/산출물은 커밋하지 않는다.

## 커밋 1. `chore: repo hygiene and build setup`

### 이유

- 이후 커밋에서 생성되는 산출물이 다시 추적되지 않도록 기준을 먼저 만든다.
- Gradle 실행 스크립트와 wrapper 버전을 고정해야 다음 커밋부터 동일한 환경으로 빌드할 수 있다.

### 커밋할 파일

- `.gitignore`
- `build.gradle`
- `gradle/wrapper/*`
- `gradlew`
- `gradlew.bat`

### 제외할 파일

- `.idea/*`
- `*.iml`
- `build/*`
- `.gradle/*`

### 커밋 중간에 수정할 파일

- `build.gradle`
  - QueryDSL, Lombok, Spring Security, JPA 관련 의존성 확인
  - `compileJava` 성공 여부에 따라 generated source 경로 조정
- `.gitignore`
  - `build/`, `.gradle/`, `.idea/`, `*.iml`, `*.class` 포함 여부 재확인

---

## 커밋 2. `chore: common bridge baseline`

### 이유

- `user-service` 내부에서만 쓰는 공통 코드를 user 도메인과 분리해 관리하기 위한 브릿지다.
- 공통 응답, 예외, 보안은 `user` 도메인 구현과 분리해서 유지한다.

### 커밋할 파일

- `common/**`
- `UserServiceApplication.java`

### 제외할 파일

- `user/**`
- `docs/*`

### 커밋 중간에 수정할 파일

- `common/security/JwtClaimSupport.java`
  - user-service 내부 JWT 클레임 구조가 확정되면 수정 필요
- `common/exception/GlobalExceptionHandler.java`
  - user-service 내부 예외 매핑 규칙과 맞춰 재정리 필요
- `build.gradle`
  - user-service 내부 공통 코드 구조와 맞춰 다시 수정 가능

---

## 커밋 3. `refactor: user domain and dto mapping`

### 이유

- 핵심 비즈니스 로직은 이 저장소의 중심이므로, 엔티티 생성과 DTO 변환 규칙을 먼저 고정한다.
- 엔티티는 정적 팩토리 메서드로 생성하고, 매핑은 DTO 단계에서만 하도록 책임을 분리한다.

### 커밋할 파일

- `user/application/**`
- `user/domain/**`
- `user/presentation/**`

### 제외할 파일

- `common/**`
- `user/infrastructure/persistence/jpa/**`

### 커밋 중간에 수정할 파일

- `user/application/UserService.java`
  - 서비스 내부의 수동 매핑 helper 제거
  - 응답 변환은 `Result.from(...)`으로 통일
- `user/domain/entity/User.java`
  - `create(...)` 정적 팩토리 유지 여부 확인
  - `@PrePersist`, `@PreUpdate` 동작 확인
- `user/application/dto/result/*`
  - `maskAccountNumber` 같이 DTO 내부에서만 필요한 로직 점검
- `user/presentation/*`
  - 컨트롤러가 `new ResponseDto(...)` 대신 `ResponseDto.from(...)`만 쓰는지 확인

---

## 커밋 4. `refactor: querydsl persistence layer`

### 이유

- 조회 로직은 repository 구현체에서 QueryDSL로 분리하는 편이 변경 범위가 작고 테스트하기 쉽다.
- 도메인과 presentation이 먼저 안정화된 뒤 인프라 최적화를 붙이는 순서가 안전하다.

### 커밋할 파일

- `user/infrastructure/persistence/jpa/**`

### 제외할 파일

- `common/**`
- `user/presentation/**`

### 커밋 중간에 수정할 파일

- `user/infrastructure/persistence/jpa/UserRepositoryImpl.java`
  - pageable 조건, 정렬 기준, count 쿼리 확인
- `user/infrastructure/persistence/jpa/AccountRepositoryImpl.java`
  - 기본 계좌 조회와 default 해제 로직 확인
- `user/infrastructure/persistence/jpa/AddressRepositoryImpl.java`
  - 기본 배송지 조회와 default 해제 로직 확인
- `user/infrastructure/persistence/jpa/ReportRepositoryImpl.java`
  - 상태 필터와 페이지 계산 방식 확인

---

## 커밋 순서 요약

1. 빌드/무시 규칙 고정
2. 공통 브릿지 연결
3. user 도메인과 DTO 매핑 정리
4. QueryDSL 저장소 구현 정리

## 커밋 제외 기준

- `.idea/**`
- `*.iml`
- `build/**`
- `.gradle/**`
- `docs/**`는 코드 커밋과 분리할 때만 포함

## 최종 점검

- `./gradlew compileJava`
- `./gradlew test`
- staged 목록에 `build/` 산출물이 없는지 확인
- `common/**`와 `user/**` 책임이 섞이지 않았는지 확인

