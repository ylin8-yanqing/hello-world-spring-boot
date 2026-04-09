# Hello World Spring Boot – SDLC Demo

**Jira Epic:** [YSJP-211](https://galactic-council.atlassian.net/browse/YSJP-211) – Hello World Java Spring Boot Web Application (SDLC Demo)

**Confluence:**
- [BRD](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583630850)
- [HLSA](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583958530)

---

## User Stories

| Story | Summary | Status |
|-------|---------|--------|
| [YSJP-218](https://galactic-council.atlassian.net/browse/YSJP-218) | Implement minimal Spring Boot REST endpoint (GET /hello) | To Do |
| [YSJP-220](https://galactic-council.atlassian.net/browse/YSJP-220) | Project scaffolding for Hello World Spring Boot (Java 22) | To Do |
| [YSJP-221](https://galactic-council.atlassian.net/browse/YSJP-221) | Implement GET /hello endpoint returning Hello World | To Do |
| [YSJP-213](https://galactic-council.atlassian.net/browse/YSJP-213) | Develop unit tests for controller and service logic | To Do |
| [YSJP-217](https://galactic-council.atlassian.net/browse/YSJP-217) | Create integration/system tests for REST endpoint | To Do |
| [YSJP-222](https://galactic-council.atlassian.net/browse/YSJP-222) | Add unit and integration tests for /hello and wire into CI | To Do |
| [YSJP-214](https://galactic-council.atlassian.net/browse/YSJP-214) | Apply baseline security best practices (HLSA §6) | To Do |
| [YSJP-215](https://galactic-council.atlassian.net/browse/YSJP-215) | Set up GitHub repository and CI pipeline with GitHub Actions | To Do |
| [YSJP-212](https://galactic-council.atlassian.net/browse/YSJP-212) | Generate and publish test reports (JUnit + Confluence summary) | To Do |
| [YSJP-219](https://galactic-council.atlassian.net/browse/YSJP-219) | Implement demo-grade deployment mechanism and post-deploy tests | To Do |
| [YSJP-216](https://galactic-council.atlassian.net/browse/YSJP-216) | Document BRD and HLSA in Confluence and maintain traceability | To Do |

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java (Temurin recommended) | 22 |
| Maven | 3.9+ |
| Docker | 24+ (for deployment) |
| Docker Compose | v2 (for deployment) |

---

## Project Structure

```
hello-world-spring-boot/
├── .github/
│   ├── dependabot.yml                          # YSJP-214: Automated dependency updates
│   └── workflows/
│       └── ci.yml                              # YSJP-215/222: CI pipeline
├── deploy/
│   └── docker-compose.yml                      # YSJP-219: Demo deployment
├── docs/
│   ├── endpoint-contract.md                    # YSJP-216/221: Endpoint contract
│   └── traceability.md                         # YSJP-216: Full traceability matrix
├── scripts/
│   └── post-deploy-test.sh                     # YSJP-219: Post-deploy smoke tests
├── src/
│   ├── main/
│   │   ├── java/com/example/helloworld/
│   │   │   ├── HelloWorldApplication.java      # YSJP-220: Entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java         # YSJP-214: Security headers + rules
│   │   │   ├── controller/
│   │   │   │   └── HelloController.java        # YSJP-218/221: GET /hello
│   │   │   ├── logging/
│   │   │   │   └── RequestLoggingConfig.java   # YSJP-218: Structured request logging
│   │   │   ├── model/
│   │   │   │   └── HelloResponse.java          # YSJP-221: Response record
│   │   │   └── service/
│   │   │       └── HelloService.java           # YSJP-218/221: Business logic
│   │   └── resources/
│   │       ├── application.properties          # YSJP-220/214: Security-hardened config
│   │       └── logback-spring.xml              # YSJP-214/218: Structured logging
│   └── test/
│       └── java/com/example/helloworld/
│           ├── controller/
│           │   ├── HelloControllerTest.java        # YSJP-213/222: Unit – controller slice
│           │   ├── HelloControllerSecurityTest.java# YSJP-213/214: Unit – security behaviour
│           │   └── HelloServiceTest.java           # YSJP-213/222: Unit – service
│           └── integration/
│               ├── HelloEndpointIT.java            # YSJP-217/222: IT – endpoint contract
│               └── HelloSecurityHeadersIT.java     # YSJP-214/217: IT – security headers
├── .gitignore
├── Dockerfile                                  # YSJP-219/214: Multi-stage, non-root
├── pom.xml                                     # YSJP-220: Java 22, pinned deps
└── README.md
```

---

## Build & Run

### Build
```bash
cd hello-world-spring-boot
mvn clean package
```

### Run locally
```bash
mvn spring-boot:run
# OR
java -jar target/hello-world-spring-boot-0.0.1-SNAPSHOT.jar
```

App starts on **http://localhost:8080**.

```bash
curl -s http://localhost:8080/hello
# → {"message":"Hello World"}
```

---

## Testing

### Unit tests only (`mvn test`) – YSJP-213/222
```bash
mvn test
```
Runs `HelloControllerTest`, `HelloControllerSecurityTest`, `HelloServiceTest`.
Reports → `target/surefire-reports/`

### Unit + Integration tests (`mvn verify`) – YSJP-217/222
```bash
mvn verify
```
Runs all unit tests **plus** `HelloEndpointIT`, `HelloSecurityHeadersIT`
(starts full app on random port over real HTTP).
Reports → `target/failsafe-reports/`

| Phase | Command | Test pattern | Maven plugin |
|-------|---------|--------------|--------------|
| Unit | `mvn test` | `*Test.java` | maven-surefire-plugin |
| Integration | `mvn verify` | `*IT.java` | maven-failsafe-plugin |

---

## Deployment (YSJP-219)

### Docker
```bash
# Build image
docker build -t hello-world-spring-boot:latest .

# Run container
docker run -p 8080:8080 hello-world-spring-boot:latest
```

### Docker Compose (demo)
```bash
docker compose -f deploy/docker-compose.yml up --build
```

### Post-deploy smoke tests
```bash
bash scripts/post-deploy-test.sh http://localhost:8080
```
Checks: HTTP 200, Content-Type, `{"message":"Hello World"}` body, no stack trace leakage, security headers.

---

## CI/CD Pipeline (YSJP-215/212)

GitHub Actions (`.github/workflows/ci.yml`) runs on every **push** and **PR** to `main`:

| Job | What it does | Artifacts |
|-----|-------------|-----------|
| `unit-tests` | `mvn test` – unit tests | `unit-test-reports/` (JUnit XML) |
| `integration-tests` | `mvn verify` – integration tests + test summary | `integration-test-reports/` (JUnit XML) + GitHub Step Summary |
| `security-scan` | OWASP Dependency-Check SCA scan | `dependency-check-report.html` |
| `post-deploy-tests` | Docker build → start container → smoke tests | Console output |

Dependabot (`.github/dependabot.yml`) automatically opens PRs for Maven and GitHub Actions dependency updates weekly.

---

## Endpoint Contract

See [`docs/endpoint-contract.md`](docs/endpoint-contract.md) for full details.

| Field | Value |
|-------|-------|
| Method | `GET` |
| Path | `/hello` |
| Status | `200 OK` |
| Content-Type | `application/json` |
| Body | `{"message":"Hello World"}` |

---

## Security (YSJP-214 / HLSA §6)

See [`docs/traceability.md`](docs/traceability.md) for the full security controls traceability matrix.

| Control | Implementation |
|---------|---------------|
| Dependency scanning | `dependabot.yml` + OWASP Dependency-Check in CI |
| Pinned dependency versions | Spring Boot BOM + explicit plugin versions in `pom.xml` |
| Security response headers | `SecurityConfig.java` – X-Content-Type-Options, X-Frame-Options, CSP, HSTS, Referrer-Policy |
| No actuator endpoints | Not on classpath; `management.endpoints.enabled-by-default=false` |
| No stack traces in responses | `server.error.include-stacktrace=never` |
| No sensitive data in logs | `logback-spring.xml` + `RequestLoggingConfig.java` |
| No secrets in code/git | `.gitignore` excludes `.env`/secret files |
| Non-root container runtime | `Dockerfile` – `appuser` non-root |
| Read-only container filesystem | `docker-compose.yml` – `read_only: true` |
| Least-privilege default | `SecurityConfig` – deny all except `/hello` |

> ⚠️ **Never commit secrets, API keys, or passwords** to this repository.

---

## Traceability

See [`docs/traceability.md`](docs/traceability.md) for full story → file mapping and HLSA §6 controls traceability.
