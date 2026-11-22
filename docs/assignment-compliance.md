# SE/SSE assignment compliance

Checklist against the published exercise and hygiene requirements.

## Spec-driven workflow

| Step | Artefact / evidence |
|------|---------------------|
| Requirement | [`spec/requirements.md`](../spec/requirements.md) |
| Specification | [`spec/`](../spec/) (`architecture`, `data-model`, `api-contract`, `state-machine`, `ui-flow`, `test-strategy`) |
| Plan / Tasks | [`specs/001-support-ticket-management/plan.md`](../specs/001-support-ticket-management/plan.md), [`tasks.md`](../specs/001-support-ticket-management/tasks.md) |
| Implementation | [`backend/`](../backend/), [`frontend/`](../frontend/) |
| Testing | [`spec/test-strategy.md`](../spec/test-strategy.md), `backend/src/test/java/...` |
| Review | [`.cursor/commands/review-code.md`](../.cursor/commands/review-code.md), [`docs/ai-review-log.md`](ai-review-log.md) |
| Fix | Corrections documented in `ai-review-log.md` and specs |

## Generic AI steering files

| Required | Location | Status |
|----------|----------|--------|
| Java Spring Boot guidelines | [`rules/java-springboot.md`](../rules/java-springboot.md) | Present |
| Testing guidelines | [`rules/testing.md`](../rules/testing.md) | Present |
| API standards | [`rules/api-standards.md`](../rules/api-standards.md) | Present |
| Documentation skill | [`.cursor/skills/documentation/SKILL.md`](../.cursor/skills/documentation/SKILL.md) | Present |
| Review code command | [`.cursor/commands/review-code.md`](../.cursor/commands/review-code.md) | Present |
| Review spec command | [`.cursor/commands/review-spec.md`](../.cursor/commands/review-spec.md) | Present |
| Generate tests command | [`.cursor/commands/generate-tests.md`](../.cursor/commands/generate-tests.md) | Present |

## Prompt history

| Required | Location | Status |
|----------|----------|--------|
| SpecStory folder | [`.specstory/history/`](../.specstory/history/) | Present |
| Prompt log | [`docs/prompt-history.md`](prompt-history.md) | Present |

## Core acceptance criteria (application)

| Criterion | Verification |
|-----------|----------------|
| Create ticket from UI | `/tickets/new` + `TicketApiIntegrationTest` |
| List tickets | Home page + API GET |
| View details | `/tickets/[id]` |
| Update fields / assignee | PUT API + UI |
| Comments | POST comments + UI |
| Search | `keyword` query + UI |
| Status filter | `status` query + UI |
| Valid transitions | `TicketStateMachineIntegrationTest` |
| Invalid transitions rejected | 409 + integration tests |
| Data survives restart | H2 file store + `TicketPersistenceIntegrationTest` |
| Backend validation | Bean Validation + 400 tests |
| UI meaningful errors | `ErrorBanner` + field errors |
| State-machine tests pass | `./gradlew test` |
| No secrets committed | env/profiles only; see `.gitignore` |

## AI as assistant (not blind acceptance)

See [`docs/ai-review-log.md`](ai-review-log.md) for documented incorrect AI suggestions and corrections.