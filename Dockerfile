# 1단계: Java 17 환경에서 Spring Boot 애플리케이션을 빌드한다.
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Gradle Wrapper와 Gradle 설정 파일을 복사한다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Linux 환경에서 gradlew를 실행할 수 있도록 권한을 설정한다.
RUN chmod +x gradlew

# Java 소스와 resources를 복사한다.
COPY src src

# 실행 가능한 Spring Boot JAR 파일을 생성한다.
RUN ./gradlew bootJar --no-daemon


# 2단계: 생성된 JAR 파일을 Java 17 환경에서 실행한다.
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build \
    /app/build/libs/week6_sb_crud_web-0.0.1-SNAPSHOT.jar \
    app.jar

# Render Web Service의 기본 포트
EXPOSE 10000

# Render가 제공하는 PORT로 Spring Boot를 실행한다.
CMD ["sh", "-c", "java -Dserver.port=${PORT:-10000} -jar app.jar"]