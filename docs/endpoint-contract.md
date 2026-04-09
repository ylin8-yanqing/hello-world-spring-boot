# Endpoint Contract – GET /hello

**Story:** [YSJP-221](https://galactic-council.atlassian.net/browse/YSJP-221) / [YSJP-218](https://galactic-council.atlassian.net/browse/YSJP-218)
**Epic:** [YSJP-211](https://galactic-council.atlassian.net/browse/YSJP-211)
**Confluence BRD:** [Link](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583630850)
**Confluence HLSA:** [Link](https://galactic-council.atlassian.net/wiki/spaces/YanConflue/pages/583958530)

---

## Request

| Field | Value |
|---|---|
| Method | `GET` |
| Path | `/hello` |
| Authentication | None (public endpoint) |
| Request body | None |
| Query parameters | None |

---

## Success Response

| Field | Value |
|---|---|
| HTTP Status | `200 OK` |
| Content-Type | `application/json` |
| Body | `{"message":"Hello World"}` |

### Example

```bash
curl -s http://localhost:8080/hello
```

```json
{"message":"Hello World"}
```

---

## Response Headers (Security)

| Header | Value | Purpose |
|---|---|---|
| `X-Content-Type-Options` | `nosniff` | Prevent MIME sniffing |
| `X-Frame-Options` | `DENY` | Prevent clickjacking |
| `Content-Security-Policy` | `default-src 'none'; frame-ancestors 'none'` | Restrict resource loading |
| `Strict-Transport-Security` | `max-age=31536000; includeSubDomains` | Enforce HTTPS (deployed envs) |
| `Referrer-Policy` | `no-referrer` | Control referrer information |

---

## Error Responses

| Scenario | HTTP Status | Notes |
|---|---|---|
| Unknown path | `403 Forbidden` | All unrecognised paths denied by default |
| Server error | `500 Internal Server Error` | No stack trace or internal details exposed |

---

## Stability Guarantee

This endpoint contract is stable and consistent across:
- Source code (`HelloController`, `HelloService`, `HelloResponse`)
- Unit tests (`HelloControllerTest`, `HelloServiceTest`)
- Integration tests (`HelloEndpointIT`, `HelloSecurityHeadersIT`)
- Post-deploy smoke tests (`scripts/post-deploy-test.sh`)
- Confluence BRD / HLSA documentation
