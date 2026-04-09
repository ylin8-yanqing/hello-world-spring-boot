# Dockerfile – Hello World Spring Boot (YSJP-219)
# YSJP-214 / HLSA §6 build/runtime hardening:
#   - Multi-stage build for minimal image
#   - Non-root user at runtime
#   - Supported Temurin JRE base image

# ── Stage 1: Build ────────────────────────────────────────────────────────
FROM eclipse-temurin:22-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper + pom first (layer-cache friendly)
COPY pom.xml .
COPY src ./src

# Build the fat JAR (skip tests – tests run separately in CI)
RUN apk add --no-cache maven && \
    mvn --batch-mode package -DskipTests

# ── Stage 2: Runtime ──────────────────────────────────────────────────────
FROM eclipse-temurin:22-jre-alpine AS runtime

# YSJP-214 / HLSA §6: Run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy only the fat JAR from build stage (minimal image)
COPY --from=builder /build/target/hello-world-spring-boot-*.jar app.jar

# Set file ownership
RUN chown appuser:appgroup app.jar

USER appuser

# Expose application port
EXPOSE 8080

# YSJP-214: JVM flags – enable runtime warnings, use container memory limits
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
