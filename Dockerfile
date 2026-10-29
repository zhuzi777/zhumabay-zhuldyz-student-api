# ---------- Stage 1: build the JAR with Gradle (official Eclipse Temurin JDK) ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy the Gradle wrapper first so the dependency layer is cached between builds
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null

# Copy the source code and build the Spring Boot JAR (tests run in Jenkins "Test" stage)
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# ---------- Stage 2: small runtime image with only the JRE and the JAR ----------
FROM eclipse-temurin:21-jre
LABEL maintainer="Zhuldyz Zhumabay <IT2-2308>" \
      org.opencontainers.image.title="zhumabay-zhuldyz-student-api" \
      org.opencontainers.image.description="Spring Boot Student CRUD API by Zhuldyz Zhumabay"

WORKDIR /app
RUN useradd --system --create-home zhumabay
COPY --from=build /workspace/build/libs/zhumabay-zhuldyz-student-api.jar /app/app.jar
USER zhumabay

ENV STUDENT_NAME="Zhuldyz Zhumabay" \
    SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
