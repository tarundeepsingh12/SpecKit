# AI review log

Engineering review of AI-generated suggestions. Each entry records a **wrong or risky AI output** and the **correction** we applied.

## Entry format

**Date** · **Area** · **AI mistake** · **Correct approach** · **Evidence**

---

## 2026-11-21 · HTTP semantics

**AI mistake:** Generated integration tests asserting **HTTP 200** on `POST /api/v1/tickets` for successful create.

**Correct approach:** Use **201 Created** per [`rules/api-standards.md`](../rules/api-standards.md) and [`spec/api-contract.md`](../spec/api-contract.md).

**Evidence:** `TicketApiIntegrationTest` asserts status 201; [`build-test-suite` command](../.cursor/commands/generate-tests.md) lists this as a known AI pitfall.

---

## 2026-11-21 · Schema management

**AI mistake:** Suggested `spring.jpa.hibernate.ddl-auto: update` for faster local iteration.

**Correct approach:** **`validate`** plus **Flyway** migrations only (NFR-003). Schema changes belong in `db/migration/V*.sql`.

**Evidence:** [`rules/java-springboot.md`](../rules/java-springboot.md), `application.yml`.

---

## 2026-11-21 · State machine coverage

**AI mistake:** Proposed a single happy-path status test (`OPEN` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`) and skipped invalid edges.

**Correct approach:** Matrix test for **every** forbidden transition (e.g. `CLOSED` → `OPEN`, `RESOLVED` → `OPEN`) returning **409**.

**Evidence:** `TicketStateMachineIntegrationTest`, [`spec/state-machine.md`](../spec/state-machine.md) §6.

---

## 2026-11-21 · Error responses

**AI mistake:** Returned generic 500 with exception message in JSON for validation failures.

**Correct approach:** **400** with `fieldErrors[]` via `GlobalExceptionHandler`; never expose stack traces to clients (FR-010).

**Evidence:** `ErrorResponse`, `ErrorBanner.tsx`.

---

## 2026-11-21 · Repository / product scope

**AI mistake:** Pointed Gradle and templates at an unrelated microservice layout from another product domain.

**Correct approach:** Monorepo `backend/` (`com.speckit.tickets`) + `frontend/` only.

**Evidence:** [`specs/001-support-ticket-management/research.md`](../specs/001-support-ticket-management/research.md); templates under `.specify/templates/`.

---

## 2026-11-21 · One-shot implementation prompt

**AI mistake:** When asked to "build the complete application" in a single prompt, produced oversized diffs, missing spec traceability, and untested state-machine gaps.

**Correct approach:** Follow spec-driven phases; use commands **review-spec** → **implement tasks** → **generate-tests** → **review-code**.

**Evidence:** Workflow in [`spec/requirements.md`](../spec/requirements.md); this repository structure.