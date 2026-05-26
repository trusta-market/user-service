# ──────────────────────────────────────────────────────────────
# Builder — Gradle 로 bootJar 생성. GitHub Packages 의 trusta common 의존성 fetch 위해
# GPR_USER / GPR_TOKEN 을 BuildKit secret 으로 주입 (ARG 사용 시 레이어 캐시에 잔존하는 문제 방지)
# workflow: docker build --secret id=gpr_user,env=GPR_USER --secret id=gpr_token,env=GPR_TOKEN
# ──────────────────────────────────────────────────────────────
FROM gradle:8.10-jdk21-alpine AS builder
WORKDIR /workspace

COPY settings.gradle build.gradle ./
COPY gradle ./gradle

RUN --mount=type=secret,id=gpr_user \
    --mount=type=secret,id=gpr_token \
    GPR_USER=$(cat /run/secrets/gpr_user) \
    GPR_TOKEN=$(cat /run/secrets/gpr_token) \
    gradle dependencies --no-daemon

COPY src ./src

RUN --mount=type=secret,id=gpr_user \
    --mount=type=secret,id=gpr_token \
    GPR_USER=$(cat /run/secrets/gpr_user) \
    GPR_TOKEN=$(cat /run/secrets/gpr_token) \
    gradle bootJar --no-daemon \
 && cp build/libs/*.jar /workspace/app.jar

# ──────────────────────────────────────────────────────────────
# Runtime — JRE 만 포함한 경량 이미지
# ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -g 1000 -S spring && adduser -u 1000 -S spring -G spring

COPY --from=builder --chown=spring:spring /workspace/app.jar /app/app.jar

USER spring:spring

EXPOSE 8081

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
