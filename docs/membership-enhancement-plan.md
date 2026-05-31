# Membership 고도화 구현 계획

---

## 개요

Order Service에서 거래가 `CONFIRMED` 되면 Kafka 이벤트를 수신해
Buyer / Seller 양쪽에 포인트를 지급하고, **최근 3개월 롤링 포인트** 기준으로
멤버십 등급을 자동 갱신하는 시스템.

---

## 서비스별 역할 분리

### User Service (이 레포 — 구현 대상)

| 역할 | 내용 |
|------|------|
| Kafka Consumer | `order.confirmed` 토픽 구독 |
| 포인트 지급 | Buyer / Seller 각각 포인트 계산 후 저장 |
| 등급 재계산 | 포인트 지급 직후 즉시, 매일 자정 스케줄러로도 실행 |
| 이력 관리 | 포인트 지급 이력, 등급 변동 이력 저장 |
| Internal API | `/internal/users/{userId}/membership/fee-rate` 수수료율 제공 |
| DB 마이그레이션 | `user_membership_point_history` 테이블 (develop 머지 전 작성) |

### Order Service (별도 레포 — 별도 구현)

| 역할 | 내용 |
|------|------|
| Kafka Producer | 주문 상태 → `CONFIRMED` 시 이벤트 발행 |
| 수수료 조회 | 결제 처리 시 Feign → user-service fee-rate API 호출 |

### Common 라이브러리 (`trustamarket/common` — 별도 반영)

| 역할 | 내용 |
|------|------|
| 공유 DTO | `OrderConfirmedEvent` record 추가 |

---

## 비즈니스 규칙

### 등급 체계

| 등급 | 3달 롤링 필요 포인트 | 수수료율 |
|------|---------------------|---------|
| BRONZE | 0 ~ 149 (기본값) | 5% |
| SILVER | 150 ~ 499 | 4% |
| GOLD | 500 ~ 999 | 3% |
| PLATINUM | 1,000 ~ 2,999 | 2% |
| DIAMOND | 3,000 이상 | 1% |

> **3달 롤링**: 오늘 기준 최근 3개월 이내 획득한 포인트만 합산해 등급 판정.
> 3개월이 지난 포인트는 자동 만료 → 등급이 자연스럽게 하락.

### 거래별 포인트 계산

| 결제 금액 구간 | 지급 비율 |
|--------------|---------|
| 1원 ~ 49,999원 | 1,000원당 1점 |
| 50,000원 ~ 99,999원 | 1,000원당 2점 |
| 100,000원 ~ 499,999원 | 1,000원당 3점 |
| 500,000원 이상 | 1,000원당 4점 |

- 구간 전체에 단일 요율 적용 (누진 아님)
- 소수점 버림: `(amount / 1000) * rate`
- Buyer / Seller 동일 금액 / 동일 계산식으로 각각 지급

**예시**

```
거래 금액 150,000원 → 구간: 100,000~499,999 → 1,000원당 3점
지급 포인트: (150,000 / 1,000) * 3 = 450점  (buyer에게 450점, seller에게 450점)
```

### 등급 재계산 시점

1. **즉시** — Kafka 이벤트 수신 후 포인트 지급 직후 (buyer, seller 각각)
2. **매일 자정** — 오늘 기준 딱 3개월 전 포인트가 만료되는 유저만 대상으로 재계산

---

## 전체 흐름

```
[Order Service]
  주문 상태 → CONFIRMED
       │
       ├─ Feign → GET /internal/users/{sellerId}/membership/fee-rate
       │          수수료율 받아서 수수료 계산 후 결제 처리
       │
       └─ Kafka produce → 토픽: order.confirmed
                          payload: { orderId, buyerId, sellerId, amount }

[User Service]
  Kafka consume ← order.confirmed
       │
       ├─ buyer 포인트 계산 → UserMembershipPointHistory 저장
       │  → 최근 3달 합산 → 등급 재계산 → User.membership 업데이트
       │  → 등급 변동 시 UserMembershipHistory 저장
       │
       └─ seller 동일 처리

[Scheduler - 매일 자정]
  오늘 기준 딱 3달 전 포인트 만료 유저 조회
       │
       └─ 만료 유저 각각 등급 재계산 → 강등 발생 시 UserMembershipHistory 저장
```

---

## 구현 파일 목록

### 수정

| 파일 | 변경 내용 |
|------|---------|
| `user/domain/vo/Membership.java` | DIAMOND 추가, 포인트 임계값·수수료율 내장 |
| `membership/application/port/in/MembershipUseCase.java` | `getFeeRate()` 메서드 추가 |
| `membership/application/service/MembershipService.java` | 포인트 지급·등급 재계산 로직 추가 |
| `docs/api-spec.md` | 신규 Internal API 추가 |

### 신규 생성

```
membership/
├── application/
│   └── port/in/
│       └── MembershipMaintenanceUseCase.java     스케줄러용 유스케이스
│   └── service/
│       └── MembershipMaintenanceService.java      만료 포인트 기반 등급 재계산
│
├── domain/
│   └── MembershipPointCalculator.java             포인트 계산 순수 도메인 로직
│
└── infrastructure/
    ├── kafka/
    │   ├── OrderConfirmedEventConsumer.java        Kafka Consumer
    │   └── KafkaConfig.java                       Kafka 설정
    └── scheduler/
        └── MembershipMaintenanceScheduler.java     매일 자정 실행

user/domain/entity/
└── UserMembershipPointHistory.java                거래별 포인트 이력 엔티티

user/domain/repository/
└── UserMembershipPointHistoryRepository.java      포인트 이력 조회 인터페이스

user/infrastructure/persistence/jpa/
└── UserMembershipPointHistoryRepositoryImpl.java  JPA 구현체

membership/presentation/
└── InternalMembershipApiController.java           fee-rate Internal API
```

---

## 도메인 설계

### Membership.java (수정)

```java
public enum Membership {
    BRONZE  (0,    0.05),
    SILVER  (50,   0.04),
    GOLD    (150,  0.03),
    PLATINUM(400,  0.02),
    DIAMOND (1000, 0.01);

    private final int requiredPoints;   // 해당 등급 진입 최소 포인트
    private final double feeRate;       // 수수료율

    // 3달 롤링 합산 포인트로 등급 판정
    public static Membership of(int points) {
        Membership[] values = values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (points >= values[i].requiredPoints) return values[i];
        }
        return BRONZE;
    }
}
```

### UserMembershipPointHistory.java (신규)

```java
@Entity
@Table(name = "user_membership_point_history")
public class UserMembershipPointHistory extends BaseCreatedEntity {

    UUID membershipPointHistoryId   // PK
    UUID userId                     // 포인트 지급 대상
    UUID orderId                    // 거래 식별자 (중복 지급 방지)
    PointRole role                  // BUYER / SELLER
    int earnedPoints                // 이번 거래로 지급된 포인트
    LocalDateTime createdAt         // 이 시각 기준 3달 윈도우 판정
}
```

> `orderId + role` 복합 유니크 제약으로 **중복 Kafka 이벤트 멱등성** 보장

### MembershipPointCalculator.java (신규)

```java
public class MembershipPointCalculator {

    public static int calculate(long amount) {
        int rate;
        if      (amount < 50_000)  rate = 1;
        else if (amount < 100_000) rate = 2;
        else if (amount < 500_000) rate = 3;
        else                       rate = 4;

        return (int)(amount / 1000) * rate;
    }
}
```

---

## Kafka 설정

### 토픽

| 토픽명 | 발행자 | 소비자 |
|--------|--------|--------|
| `order.confirmed` | Order Service | User Service |

### OrderConfirmedEvent (common 라이브러리에 추가)

```java
public record OrderConfirmedEvent(
    UUID orderId,
    UUID buyerId,
    UUID sellerId,
    long amount
) {}
```

### build.gradle 추가 의존성

```gradle
implementation 'org.springframework.kafka:spring-kafka'
```

---

## 스케줄러 쿼리 전략

매일 전체 유저를 다 조회하면 무거우므로,
**오늘 기준 딱 3달 전에 포인트를 획득한 이력이 있는 유저만** 대상으로 합니다.

```sql
SELECT DISTINCT user_id
FROM user_membership_point_history
WHERE created_at >= (NOW() - INTERVAL '3 months' - INTERVAL '1 day')
  AND created_at <  (NOW() - INTERVAL '3 months')
```

해당 유저들의 최근 3달 포인트 합산 후 등급 재계산 → 변동 시 이력 저장.

---

## Internal API 추가

### GET `/internal/users/{userId}/membership/fee-rate`

Order Service가 결제 처리 시 수수료율 조회에 사용.

**Response** `200`
```json
{
  "userId": "UUID",
  "membership": "BRONZE | SILVER | GOLD | PLATINUM | DIAMOND",
  "feeRate": 0.03
}
```

---

## DB 마이그레이션 계획 (develop 머지 전 작성)

로컬 개발 중에는 `ddl-auto: update`로 엔티티 생성 시 자동 테이블 생성.
**develop 브랜치 PR 올리기 전**에 아래 파일 추가 필요.

### 추가할 파일

**`src/main/resources/db/changelog/sql/V003__add_membership_point_history.sql`**

```sql
CREATE TABLE IF NOT EXISTS user_membership_point_history (
    membership_point_history_id UUID         NOT NULL,
    user_id                     UUID         NOT NULL,
    order_id                    UUID         NOT NULL,
    role                        VARCHAR(10)  NOT NULL,  -- BUYER / SELLER
    earned_points               INTEGER      NOT NULL,
    created_at                  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_user_membership_point_history
        PRIMARY KEY (membership_point_history_id),
    CONSTRAINT uq_order_role
        UNIQUE (order_id, role)                         -- 중복 지급 방지
);

CREATE INDEX idx_umph_user_created
    ON user_membership_point_history (user_id, created_at);
```

**`db.changelog-master.yaml`에 V002, V003 등록**

```yaml
databaseChangeLog:
  - include:
      file: sql/V001__init.sql
      relativeToChangelogFile: true
  - include:
      file: sql/V002__remove_unique_name.sql
      relativeToChangelogFile: true
  - include:
      file: sql/V003__add_membership_point_history.sql
      relativeToChangelogFile: true
```

---

## 구현 진행 현황

> 마지막 업데이트: 2026-05-28 / 브랜치: `feature/user-membership-develop`

| # | 작업 | 파일 | 상태 |
|---|------|------|------|
| 1 | DIAMOND 추가, 포인트 임계값·수수료율 내장 | `user/domain/vo/Membership.java` | ✅ 완료 |
| 2 | 포인트 이력 엔티티 | `user/domain/entity/UserMembershipPointHistory.java` | ✅ 완료 |
| 2 | Buyer/Seller 구분 enum | `user/domain/vo/PointRole.java` | ✅ 완료 |
| 3 | 포인트 이력 레포지토리 인터페이스 | `user/application/port/out/UserMembershipPointHistoryRepository.java` | ✅ 완료 |
| 3 | JPA 레포지토리 | `user/infrastructure/persistence/jpa/UserMembershipPointHistoryJpaRepository.java` | ✅ 완료 |
| 3 | JPA 구현체 | `user/infrastructure/persistence/jpa/UserMembershipPointHistoryRepositoryImpl.java` | ✅ 완료 |
| 4 | 포인트 계산 도메인 로직 | `membership/domain/MembershipPointCalculator.java` | ✅ 완료 |
| 5 | spring-kafka 의존성 추가 | `build.gradle` | ✅ 완료 |
| 5 | Kafka 이벤트 DTO (임시 — 추후 common 이동) | `membership/infrastructure/kafka/dto/OrderConfirmedEvent.java` | ✅ 완료 |
| 5 | Kafka Consumer Factory 설정 | `membership/infrastructure/kafka/KafkaConfig.java` | ✅ 완료 |
| 5 | 포인트 지급 UseCase 인터페이스 | `membership/application/port/in/MembershipPointUseCase.java` | ✅ 완료 |
| 5 | Kafka Consumer | `membership/infrastructure/kafka/OrderConfirmedEventConsumer.java` | ✅ 완료 |
| 6 | User 엔티티에 등급 변경 메서드 추가 | `user/domain/entity/User.java` — `updateMembership()` | ✅ 완료 |
| 6 | 포인트 지급 + 등급 재계산 로직 | `membership/application/service/MembershipService.java` | ✅ 완료 |
| 7 | 만료 포인트 기반 등급 재계산 스케줄러 | `membership/infrastructure/scheduler/MembershipMaintenanceScheduler.java` | ✅ 완료 |
| 8 | fee-rate Result DTO | `user/application/dto/result/internal/MembershipFeeRateResult.java` | ✅ 완료 |
| 8 | fee-rate Response DTO | `user/presentation/dto/response/internal/MembershipFeeRateResponse.java` | ✅ 완료 |
| 8 | `getFeeRate()` UseCase 메서드 추가 | `membership/application/port/in/MembershipUseCase.java` | ✅ 완료 |
| 8 | fee-rate 엔드포인트 추가 | `user/presentation/InternalUserApiController.java` | ✅ 완료 |
| 8 | API 명세 업데이트 | `docs/api-spec.md` | ✅ 완료 |

---

## 남은 작업 (develop 머지 전)

| # | 작업 | 담당 |
|---|------|------|
| 9 | `V003__add_membership_point_history.sql` 작성 | user-service |
| 9 | `db.changelog-master.yaml` — V002, V003 등록 | user-service |
| 10 | Config Server에 Kafka 설정 추가 (`bootstrap-servers`, `group-id`, `topic`) | 인프라 |
| 11 | `OrderConfirmedEvent` DTO → common 라이브러리로 이동 | common |
| 12 | Order Service — Kafka Producer 구현 | order-service |
| 12 | Order Service — fee-rate Feign Client 구현 | order-service |
