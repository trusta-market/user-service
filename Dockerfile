# ──────────────────────────────────────────────────────────────
# Builder — Gradle 로 bootJar 생성. GitHub Packages 의 trusta common 의존성 fetch 위해
# GPR_USER / GPR_TOKEN 을 build-arg 로 받음. workflow 의 docker build --build-arg 로 주입.
# ──────────────────────────────────────────────────────────────
FROM gradle:8.10-jdk21-alpine AS builder
WORKDIR /workspace

ARG GPR_USER
ARG GPR_TOKEN

COPY settings.gradle build.gradle ./
COPY gradle ./gradle
# ARG 는 RUN 내에서 환경변수로 접근 가능 (ENV 로 박으면 layer 에 잔존하므로 ENV 사용 X).
RUN GPR_USER="$GPR_USER" GPR_TOKEN="$GPR_TOKEN" gradle dependencies --no-daemon

COPY src ./src

RUN GPR_USER="$GPR_USER" GPR_TOKEN="$GPR_TOKEN" gradle bootJar --no-daemon \
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
