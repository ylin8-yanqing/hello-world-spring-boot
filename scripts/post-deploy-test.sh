#!/usr/bin/env bash
# Post-deploy smoke test script
# YSJP-219: Run post-deploy integration tests to validate the /hello endpoint
#
# Usage:
#   bash scripts/post-deploy-test.sh <base_url>
#   bash scripts/post-deploy-test.sh http://localhost:8080
#
# Exit codes:
#   0 – all checks passed
#   1 – one or more checks failed

set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"
ENDPOINT="${BASE_URL}/hello"
PASS=0
FAIL=0

echo "=================================================="
echo " Post-Deploy Smoke Tests"
echo " Target: ${ENDPOINT}"
echo "=================================================="

run_check() {
    local description="$1"
    local result="$2"   # "pass" or "fail"
    if [ "$result" = "pass" ]; then
        echo "  ✅ PASS: $description"
        PASS=$((PASS + 1))
    else
        echo "  ❌ FAIL: $description"
        FAIL=$((FAIL + 1))
    fi
}

# ── Check 1: HTTP status 200 ──────────────────────────────────────────────
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$ENDPOINT")
if [ "$HTTP_STATUS" = "200" ]; then
    run_check "GET /hello returns HTTP 200 (got: $HTTP_STATUS)" "pass"
else
    run_check "GET /hello returns HTTP 200 (got: $HTTP_STATUS)" "fail"
fi

# ── Check 2: Content-Type is application/json ─────────────────────────────
CONTENT_TYPE=$(curl -s -I "$ENDPOINT" | grep -i "content-type" | tr -d '\r')
if echo "$CONTENT_TYPE" | grep -qi "application/json"; then
    run_check "Content-Type contains application/json" "pass"
else
    run_check "Content-Type contains application/json (got: $CONTENT_TYPE)" "fail"
fi

# ── Check 3: Response body contains "Hello World" ─────────────────────────
BODY=$(curl -s "$ENDPOINT")
if echo "$BODY" | grep -q '"Hello World"'; then
    run_check 'Response body contains "Hello World"' "pass"
else
    run_check "Response body contains \"Hello World\" (got: $BODY)" "fail"
fi

# ── Check 4: Response body contains "message" key ────────────────────────
if echo "$BODY" | grep -q '"message"'; then
    run_check 'Response body contains "message" key' "pass"
else
    run_check 'Response body contains "message" key' "fail"
fi

# ── Check 5: No stack trace in response ───────────────────────────────────
if echo "$BODY" | grep -qi "stacktrace\|exception\|java\."; then
    run_check "Response does not leak stack trace" "fail"
else
    run_check "Response does not leak stack trace" "pass"
fi

# ── Check 6: X-Content-Type-Options header ────────────────────────────────
XCTO=$(curl -s -I "$ENDPOINT" | grep -i "x-content-type-options" | tr -d '\r')
if echo "$XCTO" | grep -qi "nosniff"; then
    run_check "X-Content-Type-Options: nosniff header present" "pass"
else
    run_check "X-Content-Type-Options: nosniff header present (got: $XCTO)" "fail"
fi

# ── Check 7: X-Frame-Options header ──────────────────────────────────────
XFO=$(curl -s -I "$ENDPOINT" | grep -i "x-frame-options" | tr -d '\r')
if echo "$XFO" | grep -qi "deny\|sameorigin"; then
    run_check "X-Frame-Options header present" "pass"
else
    run_check "X-Frame-Options header present (got: $XFO)" "fail"
fi

# ── Summary ───────────────────────────────────────────────────────────────
echo ""
echo "=================================================="
echo " Results: ${PASS} passed, ${FAIL} failed"
echo "=================================================="

if [ "$FAIL" -gt 0 ]; then
    echo "Post-deploy tests FAILED."
    exit 1
else
    echo "All post-deploy tests PASSED. ✅"
    exit 0
fi
