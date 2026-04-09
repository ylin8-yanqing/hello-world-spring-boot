# Traceability Matrix – Hello World Spring Boot (SDLC Demo)

**Epic:** [YSJP-211](https://galactic-council.atlassian.net/browse/YSJP-211) – Hello World Java Spring Boot Web Application (SDLC Demo)

**Confluence:**
- [BRD](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583630850)
- [HLSA](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583958530)

---

## Story → Implementation Mapping

| Jira Story | Summary | Key Files / Artifacts |
|---|---|---|
| [YSJP-218](https://galactic-council.atlassian.net/browse/YSJP-218) | Implement minimal Spring Boot REST endpoint (GET /hello) | `HelloController.java`, `HelloService.java`, `HelloResponse.java` |
| [YSJP-220](https://galactic-council.atlassian.net/browse/YSJP-220) | Project scaffolding (Java 22, Maven, security baseline) | `pom.xml`, `HelloWorldApplication.java`, `application.properties`, `.gitignore` |
| [YSJP-221](https://galactic-council.atlassian.net/browse/YSJP-221) | Implement GET /hello returning Hello World | `HelloController.java`, `HelloService.java` |
| [YSJP-213](https://galactic-council.atlassian.net/browse/YSJP-213) | Develop unit tests for controller and service logic | `HelloControllerTest.java`, `HelloServiceTest.java`, `HelloControllerSecurityTest.java` |
| [YSJP-217](https://galactic-council.atlassian.net/browse/YSJP-217) | Create integration/system tests for REST endpoint | `HelloEndpointIT.java`, `HelloSecurityHeadersIT.java` |
| [YSJP-222](https://galactic-council.atlassian.net/browse/YSJP-222) | Add unit and integration tests + wire into CI | All test files + `.github/workflows/ci.yml` |
| [YSJP-214](https://galactic-council.atlassian.net/browse/YSJP-214) | Apply baseline security best practices (HLSA §6) | `SecurityConfig.java`, `application.properties`, `.github/dependabot.yml`, `Dockerfile` |
| [YSJP-215](https://galactic-council.atlassian.net/browse/YSJP-215) | Set up GitHub repository and CI pipeline | `.github/workflows/ci.yml` |
| [YSJP-212](https://galactic-council.atlassian.net/browse/YSJP-212) | Generate and publish test reports (JUnit + Confluence summary) | CI artifact uploads in `ci.yml`, `scripts/post-deploy-test.sh` |
| [YSJP-219](https://galactic-council.atlassian.net/browse/YSJP-219) | Demo-grade deployment mechanism + post-deploy tests | `Dockerfile`, `deploy/docker-compose.yml`, `scripts/post-deploy-test.sh` |
| [YSJP-216](https://galactic-council.atlassian.net/browse/YSJP-216) | Document BRD and HLSA in Confluence + traceability | `docs/traceability.md`, `docs/endpoint-contract.md`, `README.md` |

---

## Security Controls Traceability (HLSA §6)

| HLSA §6 Control | Implementation |
|---|---|
| Dependency & supply-chain security | Spring Boot BOM, pinned plugin versions, `.github/dependabot.yml`, OWASP Dependency-Check in CI |
| Secure defaults – least privilege | `SecurityConfig.java` – deny-all default; non-root Docker user |
| Secure defaults – no public actuator | Actuator not on classpath; `management.endpoints.enabled-by-default=false` |
| Secure defaults – no stack traces | `server.error.include-stacktrace=never` in `application.properties` |
| Transport security – HTTPS | Enforced at reverse-proxy layer (demo: HTTP); HSTS header set |
| Security headers | `SecurityConfig.java` – X-Content-Type-Options, X-Frame-Options, CSP, HSTS, Referrer-Policy |
| Input handling – safe JSON | Jackson via Spring Boot; Java records (no unsafe deserialization) |
| Input validation | `spring-boot-starter-validation` on classpath |
| Secrets management | No secrets in code/git; `.gitignore` covers `.env`; env vars for runtime config |
| Logging – no sensitive data | `logback-spring.xml`, `RequestLoggingConfig.java` – no headers/body/query strings logged |
| Logging – structured | Structured pattern in `logback-spring.xml` |
| Build hardening – supported JRE | `eclipse-temurin:22-jre-alpine` base image |
| Runtime hardening – non-root | `Dockerfile` – `appuser` non-root user |
| Runtime hardening – container limits | `UseContainerSupport`, `MaxRAMPercentage` JVM flags in `Dockerfile` |
| Runtime hardening – read-only FS | `read_only: true` in `deploy/docker-compose.yml` |
